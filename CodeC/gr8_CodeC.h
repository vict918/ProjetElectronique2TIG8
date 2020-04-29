#include <18F458.h>
#device ADC=16

#FUSES NOWDT                 	//No Watch Dog Timer
#FUSES WDT128                	//Watch Dog Timer uses 1:128 Postscale
#FUSES NOBROWNOUT            	//No brownout reset
#FUSES NOLVP                 	//No low voltage prgming, B3(PIC16) or B5(PIC18) used for I/O

#use delay(crystal=20MHz)
#use rs232(baud=9600,parity=N,xmit=PIN_B7,rcv=PIN_B3,bits=8,stream=PORT1)

