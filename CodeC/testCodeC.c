#include <testCodeC.h>

int b_value;

int outputValueParser(int value){
   int output = 0;
   if (b_value < 10) { output = value;}
   else {
    int x = value;
    x = x%10;
    int i = value;
    i = i/10;
    
    output = x*16 + i;
   }
   return output;
}

void main()
{
  setup_low_volt_detect(FALSE);
  d_value = 0;

  while(true)
   {
		//Envoie la valeur sur l'afficheur 7 segments
		output_d(outputValueParser(d_value));
			
		if(d_value > 99){
		//Point de l'afficheur 7segments
		output_e(1); 
		}
		
		output_high(PIN_B0);
		delay_ms(150); 
		output_lower(PIN_B0);
		d_value++; 
	 }
}
