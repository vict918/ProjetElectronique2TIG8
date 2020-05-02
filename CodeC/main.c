#include <main.h>


int32 lecture(void);
int32 dist(void);
//int32 limite = 10;
//#INT_RDA
/*
void RDA_isr(void){
limite = lecture();
}
*/
void main(){
   output_high(PIN_B0);
   output_high(PIN_B1);
   //#define toint(c) ((c & 0x5F) > '9' ? c - '7' : c - '0') //conversion d'un char en int (0,9)
   setup_timer_0(RTCC_INTERNAL); // 409us (us = microseconde) overflow
   setup_timer_1(T1_INTERNAL); //13,1 ms overflow
   enable_interrupts(GLOBAL);
   setup_low_volt_detect(FALSE);
   enable_interrupts(INT_RDA);
   enable_interrupts(GLOBAL);
   delay_ms(100); //d�lai initialisation
   output_low(PIN_B0);
   output_low(PIN_B1);
   int32 distance=0;
   while(true){
      distance = dist(); //calcul de la distance
      
      /*
      if(distance<limite){ //si la distance est assez grande
         output_low(PIN_B0); //si la sortie est basse led rouge
         output_high(PIN_B1); //si la sortie est haute led verte
      }else{
         output_low(PIN_B1); //v�rifie que la led verte est bien �teinte
         output_toggle(PIN_B0); //la led rouge clignote
      }
      */
      
      
      
      if(distance <= 153){ //154(base 16) == 100(base 10)
         output_low(PIN_E0);
         output_d(distance);
         delay_ms(300);

         /*
         while(test%10 != 0){
            output_d((test));
            test --;
            if(32%10 == 0){ output_d(0x99); }
            
            delay_ms(400);
         }
         */
         //output_d((hexadecimal));
      }else{
         output_high(PIN_E0); //dist = 155
         int diz = distance/100; //1.55
         int unit = (distance - (diz*100))/10;
         output_d((diz<<4)+unit);
         delay_ms(300);
      }
      
      
    //printf("%lu\r\n",distance); //envoi du chiffre par le port Comm
      
   }
}
/*
int32 lecture(){
   char entre[5]; //buffer de cinq caract�res
   int32 sorti=0;
   int i=0,x=0,y=1; //sorti=int voulu
   gets(entre); //attend une cha�ne de caract�res finie par CR carriage return
   for(x=0;x<5;x++){ //boucle lisant le buffer
      if(entre[x]=='\r'){break;} //quand le char == \r => fin du int
   }
   for(i=x-1;i>0;i--){ //boucle traduisant les char en un int
      sorti+=toint(entre[i])*y; //la variable sorti est incr�ment�e du int* dizaine /centaine
      y*=10;
   }
   sorti+=toint(entre[0])*y; //la boucle ne veut pas se finir � z�ro alors voil� :'(
   return sorti;
   }
   */
int32 dist(){
   float time=0;
   int32 distance=0;
   output_high(pin_b7); //d�but de l�impulsion sur le trigger de la sonde � ultrasons
   
   delay_us(20);
   //delay_ms(10);
   output_low(pin_b7); //fin de l�impulsion
   while(!input(PIN_b3)); //tant que la pin b3/canrx du PIC est � low => reli�e au echo de la sonde
      set_timer1(0); //mise � z�ro du timer 1
   while(input(PIN_b3)); //tant que la pin b3/canrx du PIC est � high
      time=get_timer1(); //r�cup�ration du timer
   distance = time*0.00344; //calcul de la distance
   //distance = time*340/10000.0
   return distance;
   }
