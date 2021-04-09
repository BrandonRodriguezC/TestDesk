import re 

entero = "(?P<ENTERO>([0-9]+))"
real = "(?P<REAL>([0-9]+\.[0-9]+))"
logico = "(?P<LOGICO>(verdadero|falso))"
identificador = "(?P<IDENTIFICADOR>([a-z]+[0-9]+|[a-z]+))"
texto = "(?P<TEXTO>(\"([^\"\\\\]|\\\\.)*\"))"
parentesis = "(?P<PARENTESIS>(\(|\)))"
operador = "(?P<OPERADOR>(\^|\/|\*|\+|\-|\%|\=\=|\=\!|\>\=|\<\=|\<|\>|\&\&|\|\|))"
# ORDEN: 1 real - 2 entero - 3 logico - 4 identificador - 5 texto - 6 parentesis - 7 operador
conjunto = (real, entero, logico, identificador, texto, parentesis, operador) 
patron = '|'.join(conjunto)

#----------------------- TABLA DE SIMBOLOS EXPRESS ----------------------------
valores = [0, 0, 0, 10, 10]	
tipos = ["E", "E", "E", "E", "E"]
identificadores = ["numero", "tabla", "resultado", "repetir1", "repetir2"]
#------------------------------------------------------------------------------

class Node:
  def __init__(self,instruccionNum, linea, instruccion, salto, tipo):
    self.instruccion = instruccion
    self.linea = linea
    self.salto = salto
    self.tipo = tipo
    self.instruccionNum= instruccionNum

def main():
	file1 = open('Lista-Python.txt', 'r')
	Lines = file1.readlines()
	count = 0
	lista= []
	for line in Lines:
		count += 1
		lineS= line.strip()
		x = lineS.split('\t')
		#print(x)
		repuesto = "-"
		if len(x)>4:
			repuesto = x[4] 	
		n = Node (x[0], x[1], x[2], x[3], repuesto)
		lista.append(n)
		#print(n.instruccion, n.linea, n.salto, n.tipo)  
	indiceActual = 0
	actual = lista[indiceActual]
	while "end" not in actual.instruccion:
		if "Declaracion" in actual.tipo:
			if "=" in actual.instruccion:
				partes = actual.instruccion.split("=")
				primera_parte = re.sub(" +"," ", partes[0]).split(" ")
				tipo = primera_parte[0][0].upper()
				identificador = primera_parte[1]
				valor = re.sub(";", "", partes[1])
				declaracion(tipo, identificador, valor)
				indiceActual = indiceActual+1
				actual= lista[indiceActual]
				
def declaracion(tipo, identificador, valor):
	print(tipo, identificador, valor)
	s = separado(valor)
	print(s)
	p = posfija(s)
	print(p)
	e = evaluar(p)
	print (e)
	try:
		indice = identificadores.index(identificador)
	if tipos[indice] == "E":
		if type(e) is int:
			valores[indice]= e
		else:
			print("Error: El tipo de dato <%s> no coincide con Entero" % (type(e)))	
	elif tipos[indice] == "T":
		if type(e) is str:
			valores[indice]= e
		else:
			print("Error: El tipo de dato <%s> no coincide con Texto" % (type(e)))	
	elif tipos[indice] == "L":
		if type(e) is bool:
			valores[indice]= e
		else:
			print("Error: El tipo de dato <%s> no coincide con Logico" % (type(e)))	
	elif tipos[indice] == "R":
		if type(e) is float:
			valores[indice]= e
		else:
			print("Error: El tipo de dato <%s> no coincide con Real" % (type(e)))	
	
	except:
		print("No se encuentra el identificador <%s> a asignar <%s>" % (identificador, e) )
	
def separado(expresion):
	#print(patron)
	expresion_separada = []
	for match in re.finditer(patron, expresion):
		s= match.start()
		e= match.end()
		g= ""
		if match.group("ENTERO")!= None:
			g = "Entero"
		elif match.group("REAL")!= None:
			g= "Real"
		elif match.group("LOGICO")!= None:
			g= "Logico"
		elif match.group("IDENTIFICADOR")!= None:
			g= "Identificador"
		elif match.group("TEXTO") != None:
			g= "Texto"
		elif match.group("PARENTESIS") != None:
			g= "Parentesis"
		elif match.group("OPERADOR")!=None:
			g= "Operador"
# Revisar el lugar donde se agrega (se supone que debe ser al final)
		expresion_separada.append(match.group())
		print ('String match "%s" at %d:%d group %s' % (expresion[s:e], s, e, g))
	return expresion_separada

def posfija(expresion_separada):
	resultado= ""
	subexpresion=""
	i=0
	parentesis_izquierdo= 0
	parentesis_derecho=0
	pila = []
	
	while i < len(expresion_separada):
		if "(" in expresion_separada[i]:
			subexpresion = subexpresion + expresion_separada[i]
			if "(" in expresion_separada[i]:
				parentesis_izquierdo = parentesis_izquierdo+1
			elif ")" in expresion_separada[i]:
				parentesis_derecho = parentesis_derecho+1
			i= i+1
			while parentesis_izquierdo != parentesis_derecho:
				subexpresion = subexpresion + expresion_separada[i]
				if "(" in expresion_separada[i]:
					parentesis_izquierdo = parentesis_izquierdo+1
				elif ")" in expresion_separada[i]:
					parentesis_derecho = parentesis_derecho+1
				i= i+1		 
			subexpresion = subexpresion[1: len(subexpresion)-1]
			resultado = posfija(separado(subexpresion)) 
		elif es_dato(expresion_separada[i]):
			resultado = resultado + " "+ expresion_separada[i] 
			i= i+1
		elif len(pila)==0:
#Revisar donde se agrega (se supone que va en el top de la pila)
			pila.push(expresion_separada[i])
			i= i+1
		else:
			if jerarquia(expresion_separada[i]) < jerarquia(pila.peek()):
				resultado = resultado + " "+ pila.peek()
				pila.pop()
			elif jerarquia(expresion_separado[i]) == jerarquia(pila.peek()):
				while(!pila.isEmpty()):
					resultado = resultado + " " +pila.peek()
					pila.pop()
				pila.push(expresion_separada[i])
			elif jerarquia(expresion_separada[i]) >jerarquia(pila.peek()):
				pila.push(expresion_separda[i])
				i= i+1
	if !pila.isEmpty():
		while !pila.isEmpty():
			resultado= resultado + " "+ pila.peek()
			pila.pop()
	resultado = resultado[1: len(resultado)-1]
	return resultado
				 
def jerarquia (operador):
	nivel= 0
	if re.compile("\|\|").search(operador):
		nivel= 1
	elif re.compile("\&\&").search(operador):
		nivel=2
	elif re.compile("\=\=|\=\!|\>|\<|\<\=|\>\=").search(operador):
		nivel=3
	elif re.compile("\%").search(operador):
		nivel=4
	elif re.compile("\+|\-").search(operador):
		nivel=5
	elif re.compile("\*|\/").search(operador):
		nivel=6
	elif re.compile("\^").search(operador):
		nivel=7
	elif re.compile("\(|\)").search(operador):
		nivel=8
	else:
		nivel=0
	return nivel

def es_dato(identificador):
	conjunto = re.search(patron, identificador)
	if conjunto.group("PARENTESIS")!= None || conjunto.parentesis("OPERADOR") != None:
		return false
	return true

def evaluar(expresion_posfija):
	lista= []
	for match in re.finditer(patron, expresion):
		s= match.start()
		e= match.end()
		g= ""
		if match.group("ENTERO")!= None:
			g = "Entero"
		elif match.group("REAL")!= None:
			g= "Real"
		elif match.group("LOGICO")!= None:
			g= "Logico"
		elif match.group("IDENTIFICADOR")!= None:
			g= "Identificador"
		elif match.group("TEXTO") != None:
			g= "Texto"
		elif match.group("OPERADOR")!=None:
			g= "Operador"
		else:
			g= "Error"
		if g != "Operador":
			if g == "Identificador":
				lista.push(tomar_valor(match.group()))
			elif g == "Texto":
				lista.push(match.group()[1: len(match.group())])
			else:
				lista.push(match.group())
		elif len(lista > 1):
			opernado_2 = lista.peek()
			lista.pop()
			operando_1 = lista.peek()
			lista.pop()
			try:
				resultado = eval(""+operando_1+match.group()+operando_2)
				lista.push(resultado)
			except error:
				print("Error: Ocurrio un error de tipo <%s> al intentar operar %s%s%s" % (error, operando_1, match.group(), operando_2))
		else:
			if match.group() == "-":
				determinando_negativo = lista.peek() * -1
				lista.pop()
				lista.push(determinando_negativo)
			else:
				print("Error: aún queda un operaror <%s>" % (match.group()))
	return lista.peek()
 
def tomar_valor(identificador):
	indice= -1
	try:
		indice = identificadores.index(identificador)
	except: 
		print("Error: identificador <%s> no esta en lista" % (identificador))
		indice = -1
	if indice >= 0 && indice < len(valores):
		return 	valores[indice]

if __name__ == "__main__":
    main()

