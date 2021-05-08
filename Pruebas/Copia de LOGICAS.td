logico prueba;
entero a,b,c,d,e,f;
a = 5;
b = 12;
c = 0;
e = 15;
f = 4;

prueba = 8 > 6 || 9 > 8 && 7 > 19;
prueba = (8 > 6 || 9 > 8) && 7 > 19;
prueba = (3+5)/3+9%4 >= 9+((5+6)*3) || 18%4*9-4 != 15*4/3-6 && 12*6-18 == 45-52+8;
prueba = a>=b-f&&c<d-e;
prueba = (57-d+f<=a*(25-b)+14||67-d>(f*12)+15)&&a>c*10;
prueba = 57-d+f<=a*(25-b)+14||(67-d>(f*12)+15&&a>c*10);
prueba = e*(d-f)>45+c&&a>=85-d*a||27<=a*e-6;
prueba = falso || verdadero && verdadero && falso || falso && verdadero;


