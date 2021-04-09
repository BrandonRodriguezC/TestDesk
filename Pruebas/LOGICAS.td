logico   prueba ;
 prueba   =  8 > 6 || 9 > 8 && 7 > 19 ;
 prueba   =  (8 > 6 || 9 > 8) && 7 > 19 ;
 prueba   =  (3+5)/3+9%4 >= 9+((5+6)*3) || 18%4*9-4 != 15*4/3-6 && 12*6-18 == 45-52+8 ;
entero   a    =   5 ;
entero   b    =   12 ;
entero   c    =   0 ;
entero   d    =   23 ;
entero   e    =   15 ;
entero   f    =   4 ;
 prueba   =  a>=b-f&&c<d-e ;
 prueba   =  (57-d+f<=a*(25-b)+14||67-d>(f*12)+15)&&a>c*10 ;
 prueba   =  57-d+f<=a*(25-b)+14||(67-d>(f*12)+15&&a>c*10) ;
 prueba   =  e*(d-f)>45+c&&a>=85-d*a||27<=a*e-6 ;
 prueba   =  falso || verdadero && verdadero && falso || falso && verdadero ;


