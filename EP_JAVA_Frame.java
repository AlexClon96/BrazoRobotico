/*
--------------------------------------------------------------------------------
Brazo RobÛtico Automatizado(java)
--------------------------------------------------------------------------------
Este programa se encargar· de proporcionar una interfaz gr·fica para el
movimiento del robot, en el cual se indicar·n sus partes y al lado de cada una 
tendr· un par de botones de "derecha" e "izquierda", "arriba" y "abajo", 
"agarrar" y "soltar", dependiendo de quÈ parte del brazo sea. En el caso de la 
base, tiene un botÛn adicional ("detener"), debido a que si se presiona una 
direcciÛn, girar· continuamente hasta que se presione dicho botÛn. En cambio, 
los otros botones tienen un movimiento pausado. 
Adem·s se cuenta con un botÛn de "guardar" el cual mandar· los movimientos 
realizados manualmente por el usuario al programa en arduino.

   Sistemas Programables
   Manuel Alejandro Torres Fonseca
   Diana Priscila De la Rosa Campos
   Valentina Rojas Vazquez

   18 de Mayo del 2018
*/

//Inician las importaciones:
//importaciÛn para poder identificar los puertos de arduino
import gnu.io.CommPortIdentifier;
//importaciÛn para poder usar los puertos seriales
import gnu.io.SerialPort;
//importaciÛn para poder usar los eventos del puerto serial
import gnu.io.SerialPortEvent;
//importaciÛn para poder hacer una acciÛn con lo que recibe del puerto serial
import gnu.io.SerialPortEventListener;
//importaciÛn para manejar excepciones
import java.io.IOException;
//importaciÛn para poder recibir datos en el momento por el puerto serial 
import java.io.InputStream;
//importaciÛn para mandar datos en el momento por el puerto serial
import java.io.OutputStream;
//importaciÛn que es necesaria para usar el puerto serial
import java.util.Enumeration;
//importaciÛn para poder recibir varias excepciones a la vez
import java.util.TooManyListenersException;
//importaciÛn necesaria para poder mostrar un error en la excepciÛn
import java.util.logging.Level;
//importaciÛn que ejecuta la excepciÛn obtenida
import java.util.logging.Logger;
//importaciÛn para usar una ventana emergente
import javax.swing.JOptionPane;
//Terminan las importaciones 

//clase que reliza la comunicacion serial 
public class EP_JAVA_Frame extends javax.swing.JFrame implements SerialPortEventListener {

    //Inicia declaracion de variables que contendran los movimientos posibles
    //para ello se considero que a=base, b=hombro, c=codo, d=muÒeca, e=pinza

    //variable que contiene el movimiento derechaa
    private static final String derechaa = "derechaa";
    //variable que contiene el movimiento izquierdaa
    private static final String izquierdaa = "izquierdaa";
    //variable que contiene el movimiento stopa
    private static final String stopa = "stopa";
    //variable que contiene el movimiento upb
    private static final String upb = "upb";    
    //variable que contiene el movimiento downb
    private static final String downb = "downb";
    //variable que contiene el movimiento upc
    private static final String upc = "upc";
    //variable que contiene el movimiento downc
    private static final String downc = "downc";
    //variable que contiene el movimiento upd
    private static final String upd = "upd";
    //variable que contiene el movimiento downd
    private static final String downd = "downd";
    //variable que contiene el movimiento agarrare
    private static final String agarrare = "agarrare";
    //variable que contiene el movimiento soltare
    private static final String soltare = "soltare";
    //variable que contiene el change (*debiÛ ser save de guardar*)
    private static final String change = "change";
    //Termina declaracion de variables que contendran los movimientos posibles

    //crear variable de conexion de salida de datos
    private OutputStream output = null;
    //crear la variable de conexion de entrada para recibir en caso de un error
    private InputStream input = null;
    //crear la variable del puerto serial
    SerialPort serialPort;
    //crear variable que indica que puerto es
    private final String PUERTO = "COM5";
    //indicar variable de tiempo necesaria para la conexion con el puerto serial
    private static final int TIMEOUT = 2000;
    //indicar el rando de datos del puerto serial (debe ser igual que arduino)
    private static final int DATA_RATE = 115200; // en Baudios.

    //Metodo para inicializar los datos
    public EP_JAVA_Frame() {
        //llamar metodo de inicializacion de interfaz grafica
        initComponents();
        //llamar metodo de inicializacion de conexion 
        inicializarConexion();
    //Termina metodo para iniciar los datos
    }
   
    //Inicia metodo de conexion 
    public void inicializarConexion() {
        //se inicializa el valor del puerto a nulo
        CommPortIdentifier puertoID = null;    
        //se indica el objeto para el numero de puerto 
        Enumeration puertoEnum = CommPortIdentifier.getPortIdentifiers();
        //Inicia ciclo para saber si hay mas elemento en el puerto   
        while (puertoEnum.hasMoreElements()) {
            //se declara el objeto para identificar el puerto
            CommPortIdentifier actualPuertoID = (CommPortIdentifier) puertoEnum.nextElement();
            //condicional para rectificar que el puerto sea el de arduino
            if (PUERTO.equals(actualPuertoID.getName())) {
                //se les asigna lo que tenga ese puerto
                puertoID = actualPuertoID;
                //salir del ciclo
                break;
            //termina la condicional                
            }
        //Termina ciclo para saber si hay mas elementos en el puerto 
        }
        //condicional si el puerto esta vacio
        if (puertoID == null) {
            //manda llamar al metodo mostrar error 
            mostrarError("No se puede conectar al puerto");
            //se sale de la aplicacion 
            System.exit(ERROR);
            //termina la condicional del puerto vacio
        }

        try {
            //se le asignan los valores para que el puerto este abierto 
            serialPort = (SerialPort) puertoID.open(this.getClass().getName(), TIMEOUT);
            //agregar parametros al puerto
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE);
            //se le asigna los datos del puerto a la salida 
            output = serialPort.getOutputStream();
        //excepcion en caso que no funcione la conexion 
        } catch (Exception e) {
            //manda llamar al metodo error con el error
            mostrarError(e.getMessage());
            //sale de la aplicacion
            System.exit(ERROR);
        }

        try {
            //se le agrega lo que recibe de la conexion al serial a la variable input
            input = serialPort.getInputStream();
        //en caso que no funcione marca el error
        } catch (IOException ex) {
            //error que marca 
            Logger.getLogger(EP_JAVA_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            //al puerto serial se le asigna el escucha para recibir los datos 
            serialPort.addEventListener(this);
            //se le indica si los datos estan habilitados
            serialPort.notifyOnDataAvailable(true);
        //en caso de error marca el error
        } catch (TooManyListenersException ex) {
            //manda el error que es 
            Logger.getLogger(EP_JAVA_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   //Metodo que administra el puerto serial
    @Override
    public void serialEvent(SerialPortEvent spe) {
        //condicional si los datos del puerto serial son validos 
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            //crear arreglo de bytes por que los datos son recibidos por bytes 
            byte[] readBuffer = new byte[20];
            try {
                //crea la variable en la cual no hay bytes
                int numBytes = 0;
                //ciclo para recorrer los bytes recibidos
                while (input.available() > 0) {
                    //guarda todos los bytes del serial en la variable numBytes
                    numBytes = input.read(readBuffer);
                }
            //en caso de algun error
            } catch (IOException e) {
                //imprimir el error
                System.out.println(e);
            }
        }
    }
    
    //Metodo para mandar los datos a arduino
    private void enviarDatos(String datos) {
        try {
            //obtener los bytes guardados y mandarlos por el puerto serial
            output.write(datos.getBytes());
        //en caso de error 
        } catch (Exception e) {
            //llama al metodo que muestra el error
            mostrarError("ERROR");
            //termina el programa
            System.exit(ERROR);
        }
    }
    
    //Metodo que imprime el error recibido
    public void mostrarError(String mensaje) {
        //imprimir el error recibido 
        JOptionPane.showMessageDialog(this, mensaje, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    //Inicia evento de los botones:
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        agarrarE_ = new javax.swing.JButton();
        soltarE_ = new javax.swing.JButton();
        upD_ = new javax.swing.JButton();
        downD_ = new javax.swing.JButton();
        upC_ = new javax.swing.JButton();
        downC_ = new javax.swing.JButton();
        upB_ = new javax.swing.JButton();
        downB_ = new javax.swing.JButton();
        izqA_ = new javax.swing.JButton();
        derA_ = new javax.swing.JButton();
        stopA_ = new javax.swing.JButton();
        Change_ = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mini Interfaz Java");

        jLabel4.setText("pinza");

        jLabel5.setText("muneca");

        jLabel6.setText("codo");

        jLabel7.setText("hombro");

        jLabel8.setText("base");

        agarrarE_.setText("agarrar");
        agarrarE_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agarrarE_ActionPerformed(evt);
            }
        });

        soltarE_.setText("soltar");
        soltarE_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soltarE_ActionPerformed(evt);
            }
        });

        upD_.setText("arriba");
        upD_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upD_ActionPerformed(evt);
            }
        });

        downD_.setText("abajo");
        downD_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downD_ActionPerformed(evt);
            }
        });

        upC_.setText("arriba");
        upC_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upC_ActionPerformed(evt);
            }
        });

        downC_.setText("abajo");
        downC_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downC_ActionPerformed(evt);
            }
        });

        upB_.setText("arriba");
        upB_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upB_ActionPerformed(evt);
            }
        });

        downB_.setText("abajo");
        downB_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downB_ActionPerformed(evt);
            }
        });

        izqA_.setText("izquierda");
        izqA_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                izqA_ActionPerformed(evt);
            }
        });

        derA_.setText("derecha");
        derA_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                derA_ActionPerformed(evt);
            }
        });

        stopA_.setText("detener");
        stopA_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopA_ActionPerformed(evt);
            }
        });

        Change_.setText("Guardar");
        Change_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Change_ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(376, Short.MAX_VALUE)
                .addComponent(stopA_)
                .addGap(161, 161, 161))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(upC_))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(upD_))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(83, 83, 83)
                        .addComponent(agarrarE_))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(upB_))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(izqA_)))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(soltarE_)
                        .addComponent(downD_))
                    .addComponent(downC_)
                    .addComponent(downB_)
                    .addComponent(derA_))
                .addGap(87, 87, 87)
                .addComponent(Change_)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(agarrarE_)
                            .addComponent(soltarE_))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(upD_)
                                .addComponent(downD_)))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(upC_)
                            .addComponent(downC_))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(upB_)
                            .addComponent(downB_))
                        .addGap(39, 39, 39)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(Change_, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(izqA_)
                            .addComponent(derA_)
                            .addComponent(stopA_))))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    //Metodo al presionar el boton "derecha" de la base
    private void derA_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_derA_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(derechaa);
    }//GEN-LAST:event_derA_ActionPerformed
    
    //Metodo al presionar el boton "detener"
    private void stopA_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopA_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(stopa);
    }//GEN-LAST:event_stopA_ActionPerformed
    
    //Metodo al presionar el boton de "izquierda" de la base
    private void izqA_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_izqA_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(izquierdaa);
    }//GEN-LAST:event_izqA_ActionPerformed
    
    //Metodo al presionar el boton de "arriba" del hombro
    private void upB_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upB_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(upb);
    }//GEN-LAST:event_upB_ActionPerformed
    
    //Metodo al presionar el boton de "abajo" del hombro
    private void downB_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downB_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(downb);
    }//GEN-LAST:event_downB_ActionPerformed
    
    //Metodo al presionar el boton de "arriba" del codo
    private void upC_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upC_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(upc);
    }//GEN-LAST:event_upC_ActionPerformed
    
    //Metodo al presionar el boton "abajo" del codo
    private void downC_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downC_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(downc);
    }//GEN-LAST:event_downC_ActionPerformed
    
    //Metodo al presionar el boton de "arriba" de la mu√±eca
    private void upD_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upD_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(upd);
    }//GEN-LAST:event_upD_ActionPerformed
    
    //Metodo al presionar el boton de "abajo" de la mu√±eca
    private void downD_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downD_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(downd);
    }//GEN-LAST:event_downD_ActionPerformed
    
    //Metodo al presionar el boton "agarrar" de la pinza
    private void agarrarE_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agarrarE_ActionPerformed
        //manda llamar al metodo que envia el dato en el parametro
        enviarDatos(agarrare);
    }//GEN-LAST:event_agarrarE_ActionPerformed
    
    //Metodo al presionar el boton "soltar" de la pinza
    private void soltarE_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soltarE_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(soltare);
    }//GEN-LAST:event_soltarE_ActionPerformed
    
    //Metodo al presionar el boton "guardar"
    private void Change_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Change_ActionPerformed
       //manda llamar al metodo que envia el dato en el parametro
       enviarDatos(change);
    }//GEN-LAST:event_Change_ActionPerformed
    //Termina evento de los botones 

    //MÈtodo main
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EP_JAVA_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EP_JAVA_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EP_JAVA_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EP_JAVA_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EP_JAVA_Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Change_;
    private javax.swing.JButton agarrarE_;
    private javax.swing.JButton derA_;
    private javax.swing.JButton downB_;
    private javax.swing.JButton downC_;
    private javax.swing.JButton downD_;
    private javax.swing.JButton izqA_;
    private javax.swing.JButton soltarE_;
    private javax.swing.JButton stopA_;
    private javax.swing.JButton upB_;
    private javax.swing.JButton upC_;
    private javax.swing.JButton upD_;
    // End of variables declaration//GEN-END:variables
}
