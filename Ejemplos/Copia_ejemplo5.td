entero a,b,c;
a = leer( );
b = leer( );
c = leer( );
si (  a>b && b>c     )
{
	escribir(  a+","+b+","+c    );
}
sino
{
	si (  a>b && a>c && c>b     )
	{

		escribir(  a+","+c+","+b    );
	
	}
	sino
	{
		si ( b>a && b>c && a>c     )
		{
		
			escribir( b+","+a+","+c     );
		
		}
		sino
		{
		
			si (  b>a && b>c && c>a     )
			{
			
				escribir( b+","+c+","+a    );
			
			}
			sino
			{
				si (  c>a && a>b     )
				{
				
					escribir(  c+","+a+","+b );
				
				}
				sino
				{
				
					escribir( c+","+b+","+a  );
				
				}
			
			
			}
		
		}
	
	
	}


}


