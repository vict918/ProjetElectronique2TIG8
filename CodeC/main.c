#include <main.h>

int32 seuilLimite = 70;
/*
utilisation de ce tableau pour 2 raisons :
   1) Proteus n'allumant parfois pas l'afficher � 7seg, cela permet donc de lui
   forcer � afficher la valeur voulu (probl�me dans les fichiers dw de proteus).
   2) La conversion de d�cimal � hexad�cimal et permet donc un affichage exact
   de la valeur calcul�.
*/
int seg [] = {0x0,0x1,0x2,0x3,0x4,0x5,0x6,0x7,0x8,0x9,
               0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,
               0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,0x28,0x29,
               0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,
               0x40,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,
               0x50,0x51,0x52,0x53,0x54,0x55,0x56,0x57,0x58,0x59,
               0x60,0x61,0x62,0x63,0x64,0x65,0x66,0x67,0x68,0x69,
               0x70,0x71,0x72,0x73,0x74,0x75,0x76,0x77,0x78,0x79,
               0x80,0x81,0x82,0x83,0x84,0x85,0x86,0x87,0x88,0x89,
               0x90,0x91,0x92,0x93,0x94,0x95,0x96,0x97,0x98,0x99};
int32 distance = 0;
/*
Fonction permettant de calculer la distance, Nous envoyons une impulsion au Trigger
de la sonde � ultrason avec un d�lais de 20us, lorsque la pin_b3(Echo) est � high,
nous r�ceptionnons � ce moment la le temps parcouru, apr�s �a le calculer et enfin
l'envoyer pour permettre aux 7segments de l'afficher.
*/
int32 valeurDist(){
   float temps=0;
   int32 dist=0;
   
   output_high(pin_b7); //Impulsion envoy� sur le trigger de la sonde � ultrasons HC-sr04 
   delay_us(20); 
   output_low(pin_b7); //Fin de l'impulsion
   
   while(!input(PIN_b3)); //tant que la pin b3 est � low (pour attendre que l'impulsion revienne)
      set_timer1(0); //mise � z�ro du timer 1
      
   while(input(PIN_b3)); //tant que la pin b3 est � high (donc que l'impulsion est revenu)
      temps=get_timer1(); //r�cup�ration donc du timer (du temps)
     
   
   dist = temps*0.00344; // Calcule de la distance en sachant que la vitesse du son est de 340m/s
   return dist;
}



void main(){
   //test des leds en les allumant au d�marage
   output_high(PIN_B0);
   output_high(PIN_B1);
   
    
   setup_timer_0(RTCC_INTERNAL); 
   setup_timer_1(T1_INTERNAL);
   enable_interrupts(GLOBAL);
   setup_low_volt_detect(FALSE);
   enable_interrupts(INT_RDA);
   enable_interrupts(GLOBAL);
   //Cette fonction nous permet de convertir la limite re�ue par le code java (String en int)
   #define toint(c)  ((c) > '9' ? c - '7' : c - '0') //hexad�cimale
   
   //Les �teinte afin de permettre un d�lai lors du d�marage
   delay_ms(200); 
   output_low(PIN_B0);
   output_low(PIN_B1);
   

   while(true){ //Boucle infinie afin de constament cacluler la valeur de la distance
   
      distance = valeurDist(); //Appel � la fonction calculant donc la distance
      
      if(distance<seuilLimite){ //si la distance ne d�passe pas la limite alors :
         output_low(PIN_B1); //la led rouge est �teinte
         output_high(PIN_B0); //la led verte est allum�
      }else{
         output_low(PIN_B0); //sinon la led verte est �teinte
         output_toggle(PIN_B1); //et la led rouge clignote
      }
      
      
      
       //si la distance est <= 100 alors la PIN_E0 s'allume 
       // --> elle repr�sente le point sur le 2�me afficheur 7seg
      if(distance <= 100){
         output_low(PIN_E0);
         output_d(seg[distance]);
         delay_ms(300);
         
         
         printf("%lu\r\n",distance);//Envoie de la donn�e au code java
         delay_ms(300);
         
      }else{
         output_high(PIN_E0); 
         
         int ten = distance/100; 
         int unit = (distance - (ten*100))/10;
         output_d(((distance/100)<<4)+unit); //permet de mettre la dizaine � droite et l'unit� � cot�
         delay_ms(300);
      } 
      
      
   }
}


/*
Fonction permettant de prendre la valeur limite envoy� par le java
*/
#INT_RDA
void RDA_isr(void){
   char reception[4]; //buffer de cinq caract�res
   int32 valeurLimite=0;
   
   
   gets(reception); //C'est ici que nous attendons les char de la valeur attendu
  
   int x = 0;
   while(x < 4){ // Lecture du buffer
      if(reception[x] == '\r'){
         break; //Si la fin su char est �gale � \r cela veut dire que c'est la fin du int(g�r� en java)   
      }
      x++;
   }
   
   int i=x-1;
   int j=1; //compteur initialiser � 0
   while(i > 0){ //i >= 0 Ne marche pas
      valeurLimite += toint(reception[i])*j;
      j*=10;
      i--;
   }
   
   //ajout de la derni�re valeur manuellement
   valeurLimite += toint(reception[0])*j;
   seuilLimite = valeurLimite;
}
