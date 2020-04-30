#include <main.h>



int32 lecture(void);
int32 dist(void);

#INT_RDA

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
   delay_ms(100); //délai initialisation
   output_low(PIN_B0);
   output_low(PIN_B1);
   int32 distance=0;
   
   while(true){
      distance = dist(); //calcul de la distance
      if(distance<100){ //si la distance est assez grande
         output_low(PIN_B0); //si la sortie est basse led rouge
         output_high(PIN_B1); //si la sortie est haute led verte
         output_low(PIN_E0);
      }else{
         output_low(PIN_B1); //vérifie que la led verte est bien éteinte
         output_toggle(PIN_B0); //la led rouge clignote
         output_high(PIN_E0);
      }
      output_d(distance);
      
      delay_ms(300);
   }
}

int32 dist(){
   float time=0;
   int32 distance=0;
   output_high(pin_b7); //début de l’impulsion sur le trigger de la sonde à ultrasons
   delay_us(20);//20
   output_low(pin_b7); //fin de l’impulsion
   
   while(!input(PIN_b3)); //tant que la pin b3/canrx du PIC est à low => reliée au echo de la sonde
   set_timer1(0); //mise à zéro du timer 1
   
   while(input(PIN_b3)); //tant que la pin b3/canrx du PIC est à high
   time=get_timer1(); //récupération du timer
   //distance = (time*340)/10000;
   distance = time*0.00344; //calcul de la distance
   return distance;
}
