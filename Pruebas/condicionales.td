logico   a    =    verdadero   ;
logico   b    =    falso   ;
logico   c    =    verdadero && falso   ;
si (  a     )
{
	   b    =   verdadero    ;
	si (   a==b    )
	{
		    a   =     falso  ;
		si ( a || verdadero    )
		{
			   c    =    verdadero   ;
		
		
		}
	
	}

}
sino
{

	si (  b && c    )
	{
	
		   a    =   falso    ;
	
	}

}
