/*----------------------------------------------------------------------------
   Brazo Robotico Automatizado(Arduino)
   ---------------------------------------------------------------------------
   Este programa se emcargara de mover un brazo robotico en el cual
   sera controlado desde una interface de java que mandara instrucciones
   a este programa el cual analizara la instrucciones y las ejecutara,
   aparte usara componentes como una pantalla lcd de 16X2 en el cual
   mostrara el modo en que esta el programa de los cuales hay 2 tipos
   de modos.
   1 modo programacion:en este modo el usuario podra manipular libremente
   el brazo robotico.
   2 modo ejecucion:este es un modo automatico en el cual repetira los
   movimientos ejecutados en el modo de programacion.
   Este programa tambien contara con 2 leds(color rojo y verde),en el cual
   representara en que modo se encuentra(ejecucion,programacion)los cuales
   no pueden estar prendidos al mismo tiempo.
   Tambien contara con un boton que paro en caso de que ocurra un accidente
   o se requiera parar el brazo cuando esta en modo de ejecucion, al presionar
   el boton se activara una alarma para indicar que el brazo ha parado de hacer
   su labor.

   Sistemas Programables
   Manuel Alejandro Torres Fonseca
   Diana Priscila De la Rosa Campos 
   Valentina Rojas Vazquez

   18 de Mayo del 2018
  -----------------------------------------------------------------------------
*/
//iniciar  la importacion de librerias
//incluir lalibreria LiquidCrystal que nos permite usar la pantalla lcd
#include  <LiquidCrystal.h>
//termina la importacion de librerias

//inicia la declaracion de la variables y constantes
//en la variable caracter se guardan los bytes que manda java
char caracter;
//en la variable comando se unen todos los caracteres en 1 cadena
String comando;
//eneste arreglo se guardan todas los movimientos del brazo robotico
String arreglo[30];
//este contador indica que posision del arreglo sigue de ejecutarse
int cont = 0;
//este contador indica el  numero de movimientos que se recivieron de java
int tamArr = 0;
//esta variable nos indica cuando ya se preciono el boton de paro
boolean btnParo = true;
//se incializa la pantalla lcd con los pines correspondientes al circuito
LiquidCrystal lcd(44, 42, 40, 38, 36, 34);
//termina la declaracion de variables y constantes

//inicia el metodo setup
void setup() {
  //inicia la declaracion de pines y como se usaran en arduino
  //este pin es de salida para mover la base a la derecha
  pinMode(2, OUTPUT);
  //este pin es de salida para mover la base a la izquierda
  pinMode(3, OUTPUT);
  //este pin es de salida para mover el hombro hacia arriba
  pinMode(12, OUTPUT);
  //este pin es de salida para mover el hombro hacia abajo
  pinMode(13, OUTPUT);
  //este pin es de salida para mover el codo hacia arriba
  pinMode(6, OUTPUT);
  //este pin es de salida para mover el codo hacia abajo
  pinMode(7, OUTPUT);
  //este pin es de salida par mover la muñeca hacia arriba
  pinMode(8, OUTPUT);
  //este pin es de salida para mover la muñeca hacia abajo
  pinMode(9, OUTPUT);
  //este pin es de salida para abrir la pinza
  pinMode(10, OUTPUT);
  //este pin es de salida par cerrar la pinza
  pinMode(11, OUTPUT);
  //este pin es de entrada para el boton de paro
  pinMode(5, INPUT);
  //este pin es de salida para el led de modo de programacion
  pinMode(22, OUTPUT);
  //este pin es de salida para el led de modo de ejecucion
  pinMode(24, OUTPUT);
  //este pin es de salida para la alarma  de paro
  pinMode(26, OUTPUT);
  //termina la declaracion de pines
  //se inicia el puerto serial a 115200 baudios
  Serial.begin(115200);

  //se inicializa la pantalla lcd de 16X2
  lcd.begin(16, 2);
  //se pone el cursor de la lcd al prinicipio del primer renglon
  lcd.home();
  //muestra el mensaje ESTAS EN MODO en el primer renglon
  lcd.print("ESTA EN MODO");
  //se pasa el cursor al renglon de abajo
  lcd.setCursor(0, 1);
  //muestra el mensaje PROGRAMACION en el segundo renglon
  lcd.print("PROGRAMACION");
}
//termina el metodo setup

//inicia el metodo loop
void loop() {
  //se prende el led del modo programacion
  digitalWrite(22, HIGH);
  //Se lee carácter por carácter por el puerto serie, mientras, se va
  //concatenando uno tras otro en una cadena. */
  while (Serial.available() > 0)
  {
    caracter = Serial.read();
    comando.concat(caracter);
    delay(10); // Este retardo muy corto es para no saturar el puerto
    // serie y que la concatenación se haga de forma ordenada.
  }

  //se compara si la cadena es derechaa
  if (comando.equals("derechaa") == true)
  { //inicia si la comparacion es correcta
    //hace girar el motor de la base a la derecha
    digitalWrite(2, HIGH);
    digitalWrite(3, LOW);
    //se termina la comparacion
  }

  //se compara si la cadena es izquierda
  if (comando.equals("izquierdaa") == true)
  { //inicia si la comparacion es correcta
    //hace girar el motor de la base a la izquierda
    digitalWrite(2, LOW);
    digitalWrite(3, HIGH);
    //se termina la comparacion
  }

  //se compara si la cadena es stopa
  if (comando.equals("stopa") == true)
  { //inicia si la comparacion es correcta
    //hace que se pare el motor de la base
    digitalWrite(2, LOW);
    digitalWrite(3, LOW);
    //se termina la comparacion
  }

  //se compara si la cadena es upb
  if (comando.equals("upb") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se levante el hombro
      digitalWrite(12, LOW);
      digitalWrite(13, HIGH);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene  el motor
      digitalWrite(12, LOW);
      digitalWrite(13, LOW);
      //termina el ciclo para el movimiento pausado
    }
    //se termina la comparacion
  }

  //se compara si la cadena es downb
  if (comando.equals("downb") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se baje el hombro
      digitalWrite(12, HIGH);
      digitalWrite(13, LOW);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(12, LOW);
      digitalWrite(13, LOW);
      //termina el ciclo para el movimiento pausado
    }
    //se termina la comparacion
  }

  //se compara si la cadena es upc
  if (comando.equals("upc") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se levante el codo
      digitalWrite(6, LOW);
      digitalWrite(7, HIGH);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(6, LOW);
      digitalWrite(7, LOW);
      //termina el ciclo para el movimiento pausado
    }

    //se termina la comparacion
  }

  //se compara si la cadena es downc
  if (comando.equals("downc") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se baje el codo
      digitalWrite(6, HIGH);
      digitalWrite(7, LOW);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(6, LOW);
      digitalWrite(7, LOW);
      //termina el ciclo para el movimiento pausado
    }

    //se termina la comparacion
  }

  //se compara si la cadena es upd
  if (comando.equals("upd") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se levante la muñeca
      digitalWrite(8, LOW);
      digitalWrite(9, HIGH);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(8, LOW);
      digitalWrite(9, LOW);
      //termina el ciclo para el movimiento pausado
    }

    //se termina la comparacion
  }

  //se compara si la cadena es downd
  if (comando.equals("downd") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se baje la muñeca
      digitalWrite(8, HIGH);
      digitalWrite(9, LOW);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(8, LOW);
      digitalWrite(9, LOW);
      //termina el ciclo para el movimiento pausado
    }
    //se termina la comparacion
  }

  //se compara si la cadena es agarrare
  if (comando.equals("agarrare") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se cierra la pinza
      digitalWrite(10, LOW);
      digitalWrite(11, HIGH);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(10, LOW);
      digitalWrite(11, LOW);
      //termina el ciclo para el movimiento pausado
    }
    //se termina la comparacion
  }

  //se compara si la cadena es soltare
  if (comando.equals("soltare") == true)
  { //inicia si la comparacion es correcta
    //guarda el movimiento en el arreglo
    arreglo[cont] = comando;
    //incrementa el contador de la posicion
    cont++;
    //inicia un ciclo para que el movimiento sea pausado
    for (int a = 0; a < 2; a++) {
      //gira el motor de tal manera que se abre la pinza
      digitalWrite(10, HIGH);
      digitalWrite(11, LOW);
      //indica por cuanto tiempo se va mover
      delay(100);
      //detiene el motor
      digitalWrite(10, LOW);
      digitalWrite(11, LOW);
      //termina el ciclo para el movimiento pausado
    }
    //se termina la comparacion
  }

  //se compara si la cadena es change
  if (comando.equals("change") == true)
  { //inicia si la comparacion es correcta
    //limpia la pantalla lcd
    lcd.clear();
    //deja el cursor al principio del primer renglon
    lcd.home();
    //muestra el mensaje ESTAS EN MODO en el primer renglon de la lcd
    lcd.print("ESTA EN MODO");
    //deja el cursor en el segundo renglon
    lcd.setCursor(0, 1);
    //muestra el mensaje EJECUCION en el segundo renglon de la lcd
    lcd.print("EJECUSION");
    //apaga el led de modo programacion
    digitalWrite(22, LOW);
    //enciende el led de modo ejecucion
    digitalWrite(24, HIGH);
    //emite el sonido de la alarma por 1 segundo
    tone(26, 128, 1000);
    //contador que indica cuando hizo todos los movimientos guardados
    int a = 0;
    //inicia ciclo infinito para el modo de ejecucion
    while (true) {
      //inicia el ciclo para ejecutar los movimientos guardados
      if (a < cont) {
        //se declara el boton de paro del pin 5 del arduino
        int boton = digitalRead(5);

        //condicional cuando se oprime el boton
        if (boton == HIGH) {
          //indica que el boton ya se oprimio
          btnParo = false;
          //limpia la pantalla lcd
          lcd.clear();
          //pone el cursor al principio del primer renglon
          lcd.home();
          //muestra el mensaje SE PRECIONO EL en la lcd
          lcd.print("SE PRECIONO EL");
          //manda el cursor al segundo renglon
          lcd.setCursor(0, 1);
          //muestra el mensaje BOTON DE PARO en el segundo renglon
          lcd.print("BOTON DE PARO");
          //termina la condicion del boton
        }

        //condicional cuando no se ha apretado del boton
        if (btnParo == false) {
          //darle un valor para que no pueda entrar a las otras condicionales
          a = 31;
          //declaran variables para el tono que tendra la alarma
          float sinVal, toneVal;
          //inicia el ciclo para hacer la melodia de la alarma
          for (int x = 0; x < 180; x++) {
            // convertimos grados en radianes
            sinVal = (sin(x * (3.1412 / 180)));
            // generamos la frecuencia del sinVal
            toneVal = 2000 + (int(sinVal * 1000));
            //reproduce el tono de la alarma
            tone(26, toneVal);
            //termina el ciclo para haccer la melodia de alarma
          }
          //se espera un poco  para que la alarma no se mexcle
          delay(100);
          //se termina la comparacion
        }

        //se compara si la cadena es upb y no se ha apletado el boton de paro
        if (arreglo[a].equals("upb") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que se levante el hombro
            digitalWrite(12, LOW);
            digitalWrite(13, HIGH);
            //indica por cuanto tiempo se va mover
            delay(100);
            //detiene  el motor
            digitalWrite(12, LOW);
            digitalWrite(13, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es downb y no se ha apletado el boton de paro
        if (arreglo[a].equals("downb") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que baja el hombro
            digitalWrite(12, HIGH);
            digitalWrite(13, LOW);
            //indica por cuanto tiempo se va mover 
            delay(100);
            //para el motor
            digitalWrite(12, LOW);
            digitalWrite(13, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es upc y no se ha apletado el boton de paro
        if (arreglo[a].equals("upc") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que sube el codo
            digitalWrite(6, LOW);
            digitalWrite(7, HIGH);
            //indica por cuanto tiempo se va mover el motor
            delay(100);
            //para el motor
            digitalWrite(6, LOW);
            digitalWrite(7, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es downc y no se ha apletado el boton de paro
        if (arreglo[a].equals("downc") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que baja el codo
            digitalWrite(6, HIGH);
            digitalWrite(7, LOW);
            //indica por cuanto tiempo se va mover el motor
            delay(100);
            //para el motor
            digitalWrite(6, LOW);
            digitalWrite(7, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es upd y no se ha apletado el boton de paro
        if (arreglo[a].equals("upd") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que levanta la muñeca
            digitalWrite(8, LOW);
            digitalWrite(9, HIGH);
            //indica por cuanto tiempo se va mover el motor
            delay(100);
            //para el motor
            digitalWrite(8, LOW);
            digitalWrite(9, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es downd y no se ha apletado el boton de paro
        if (arreglo[a].equals("downd") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que baja la muñeca
            digitalWrite(8, HIGH);
            digitalWrite(9, LOW);
            //indica por cuanto tiempo se va mover el motor
            delay(100);
            //para el motor
            digitalWrite(8, LOW);
            digitalWrite(9, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es agarrare y no se ha apletado el boton de paro
        if (arreglo[a].equals("agarrare") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //gira el motor de tal manera que cierra la garra
            digitalWrite(10, LOW);
            digitalWrite(11, HIGH);
            //inidica el tiempo en que se va a mover el motor
            delay(100);
            //para el motor
            digitalWrite(10, LOW);
            digitalWrite(11, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }

        //se compara si la cadena es soltare y no se ha apletado el boton de paro
        if (arreglo[a].equals("soltare") == true && btnParo == true)
        { //inicia si la comparacion es correcta
          //inicia un ciclo para que el movimiento sea pausado
          for (int a = 0; a < 2; a++) {
            //mueve el motor de tal manera que abre la garra
            digitalWrite(10, HIGH);
            digitalWrite(11, LOW);
            //indica por cuanto tiempo se va mover el motor
            delay(100);
            //para el motor
            digitalWrite(10, LOW);
            digitalWrite(11, LOW);
            //termina el ciclo para el movimiento pausado
          }
          //indica que se hara el siguiente movimiento guardado
          a++;
          //se espera medio segundo entre movimientos
          delay(500);
          //se termina la comparacion
        }
        //termina el ciclo para ejecutar los movimientos guardados
      } else {
        //si ya se acabaron los movimientos se reinician los movimientos
        a = 0;
        //termina el ciclo de reinicio
      }
      //termina el ciclo infinito para el modo de ejecucion
    }
    //termina la condicional si la cadena es change
  }
  //deja vacia la cadena para volver a recivir datos de java
  comando = "";
  //termina el metodo loop
}
