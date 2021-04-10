package logica;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analizadores {
	private static final String ENTERO = "[0-9]+";
	private static final String REAL = "[0-9]+\\.[0-9]+";
	private static final String LOGICO = "verdadero|falso";
	private static final String TEXTO = "\"([^\"\\\\]|\\\\.)*\"";
	// private static final String TEXTO ="\".*\"";
	private static final String PARENTESIS = "\\(|\\)";
	private static final String OPERADOR = "\\^|\\/|\\*|\\+|\\-|\\%|\\=\\=|\\!\\=|\\>\\=|\\<\\=|\\<|\\>|\\&\\&|\\|\\|";
	private static final String ESPACIO = "\\s+";
	private static final String COMA = "\\,";
	private static final String IDENTIFICADOR = "[a-z]+([0-9]+)?";
	private static final String PALABRAS_PROHIBIDAS ="repetir([0-9]+)?|veces([0-9]+)?|mientras([0-9]+)?|que([0-9]+)?|si([0-9]+)?|sino([0-9]+)?|entero([0-9]+)?|real([0-9]+)?|logico([0-9]+)?|texto([0-9]+)?|end([0-9]+)?|jump([0-9]+)?|leer([0-9]+)?|escribir([0-9]+)?";

	private Pattern PATRON = Pattern.compile(
			"(?<COMA>" + COMA + ")" + "|(?<LOGICO>" + LOGICO + ")" + "|(?<REAL>" + REAL + ")" + "|(?<ENTERO>" + ENTERO
					+ ")" + "|(?<IDENTIFICADOR>" + IDENTIFICADOR + ")" + "|(?<TEXTO>" + TEXTO + ")" + "|(?<PARENTESIS>"
					+ PARENTESIS + ")" + "|(?<OPERADOR>" + OPERADOR + ")" + "|(?<ESPACIO>" + ESPACIO + ")"
	);

	private ArrayList<String> errores;
	Hashtable<Integer, ExpresionesLinea> expresionesPosfijas;
	
	TablaDeSimbolos tablaDeSimbolos;
	

	public Analizadores() {
		errores = new ArrayList<String>();
		tablaDeSimbolos = new TablaDeSimbolos();
		expresionesPosfijas = new Hashtable<Integer,ExpresionesLinea >();
	}
	
	public boolean evaluate(boolean compuesta, boolean declaracionAsignacion, String tipoDeVariable, String expresion,
			int linea, boolean repetir, boolean escribir) {
		
		errores.add((new StringBuilder()).append("LINEA ").append(linea).append(":\nExpresion: ").append(expresion).append("\nCompuesta: ").append(compuesta).append("\nDeclaracion: ").append(declaracionAsignacion).append("\nTipo: ").append(tipoDeVariable).toString());
		
		errores.add("······································");
		
		if (compuesta == true && declaracionAsignacion == true) {
			
			/** La unica parte de una declaracion simple: 
			 * 
			 * ENTERO [ ];
			 * 
			 * **/
			
			ArrayList<String> pf = analizador(compuesta, declaracionAsignacion, tipoDeVariable, expresion, linea);
			
			if (pf == null || pf.isEmpty()) {
				errores.add("######################################");
				return false;
			}
			errores.add("Se añade la expresion "+pf);
			añadirExpresionLinea(String.join(" ", pf), linea, tipoDeVariable);
			
		} else if (compuesta == true && declaracionAsignacion == false) {
			
			/** La segunda parte de una asignacion y una condicion sea while-for-if:
			 *  
			 *  ... = [ ]; 
			 *  if([   ]) 
			 *  for([   ]) 
			 *  while([   ])
			 *  
			 *  **/
			
			ArrayList<String> pf = analizador(compuesta, declaracionAsignacion, tipoDeVariable, expresion, linea);
			if (pf == null || pf.isEmpty()) {
				errores.add("######################################");
				return false;
			}
			String posfija = Posfijo.postfija(pf.toArray(new String[0]));
			
			errores.add("Se añade la expresion "+posfija);
			
			añadirExpresionLinea(posfija, linea, null);
			errores.add((new StringBuilder()).append("POST FIJO --------> ").append(posfija).toString());
			String rta = Evaluador.evaluarSemanticamente(posfija, tablaDeSimbolos);
			
			errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Se espera el tipo de dato <").append(tipoDeVariable).append("> y el tipo generado es <").append(rta).append(">").toString());
			if (rta.equals("X")) {
				errores.add("ERROR: Expresion no operable");
				errores.add("######################################");
				return false;
			} else {
				if (escribir) {
					return true;
				}
				if (rta.equals(tipoDeVariable)|| (tipoDeVariable.equals("R") && rta.equals("E"))) {
					errores.add("######################################");
					return true;
				} else {
					errores.add("ERROR: El resultado de la expresion no\ncoincide con el tipo de la variable");
					errores.add("######################################");
					return false;
				}
			}
		} else if (compuesta == false && declaracionAsignacion == true) {
			
			/** La primera parte de una Declaracion-Asignacion 
			 * 
			 *  entero [  ] = ...; 
			 * 
			 * **/
			
			ArrayList<String> pf = analizador(compuesta, declaracionAsignacion, tipoDeVariable, expresion, linea);
			if (pf == null || pf.isEmpty()) {
				errores.add("######################################");
				return false;
			}
			añadirExpresionLinea(String.join(" ", pf), linea, tipoDeVariable);
		} else if (compuesta == false && declaracionAsignacion == false) {
			
			/** La primera parte de una asignacion, la segunda parte de una declaracion:
			 * 
			 *  [  ] = ...; 
			 *  entero ... = [   ];
			 *  
			 *  **/
			
			ArrayList<String> pf =  analizador(compuesta, declaracionAsignacion, tipoDeVariable, expresion, linea);
			if (pf == null || pf.isEmpty()) {
				errores.add("######################################");
				return false;
			}
			
			String posfija = Posfijo.postfija(pf.toArray(new String[0]));
			añadirExpresionLinea(posfija, linea, null);
			errores.add((new StringBuilder()).append("POST FIJO --------> ").append(posfija).toString());
			if (posfija.matches(IDENTIFICADOR)) {
				if(tipoDeVariable.equals(encontrarTipoParaIdentificador(posfija.trim()))) {
					errores.add("######################################");
					return true;
				}else {
					errores.add("ERROR: El tipo del identificador no\ncoincide con el tipo de la variable");
					errores.add("######################################");
					return false;
				}
			}else {
				if (!posfija.isEmpty()) {
					String rta = Evaluador.evaluarSemanticamente(posfija, tablaDeSimbolos);
					if(repetir) {
						String expresionlimpia= expresion.replace(" ", "");
						if (!expresionlimpia.matches(ENTERO)) {
							errores.add("ERROR: El valor en la expresion repetir\nno es un entero");
							errores.add("######################################");
							return false;
						}
						int valor= 0;
						try {
							valor= Integer.parseInt(expresionlimpia);
						}catch (Exception e) {
							errores.add((new StringBuilder()).append("ERROR: El valor en la expresion <").append(expresion).append(",").append(pf).append("> no es valido").toString());
							errores.add("######################################");
							return false;
						}
						
						if(valor<0) {
							errores.add((new StringBuilder()).append("ERROR: El valor en la expresion <").append(expresion).append(",").append(pf).append("> repetir es menor a 0").toString());
							errores.add("######################################");
							return false;
						}else if(valor==0) {
							errores.add("ERROR: El valor en la expresion repetir\nes igual a 0");
							errores.add("######################################");
							return false;
						}else {
							return true;
						}
					}
					if (rta.equals(tipoDeVariable)) {
						errores.add("######################################");
						return true;
					} else if (tipoDeVariable.equals("R") && rta.equals("E")) {
						errores.add("######################################");
						return true;
					} else {
						errores.add("ERROR: El resultado de la expresion no\n coincide con el tipo de la variable");
						errores.add("######################################");
						return false;
					}
				}else {
					errores.add("ERROR: La expresion es vacia");
					errores.add("######################################");
					return false;	
				}
			}
		}
		errores.add("######################################");
		return true;
	}
	
//	public void informacion() {
//		errores.add(imprimirTablaDeSimbolos());
//		errores.add("######################################");
//		errores.add(imprimirExpresionesPosfijas());
//		System.out.println(imprimirExpresionesPosfijas());
//		errores.add("######################################");
//	}

	public ArrayList<String> analizador(boolean compuesta, boolean declaracionAsignacion, String tipoDeVariable, String expresion,
			int linea) {
		Matcher comparador = PATRON.matcher(expresion);
		int contador = 0, estado = 0, parentesis = 0, ultimoCaracterIdentificado = 0;
		ArrayList<String> separado = new ArrayList<>();

		while (comparador.find()) {

			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
								: comparador.group("REAL") != null ? "T-REAL"
								: comparador.group("LOGICO") != null ? "T-LOGICO"
								: comparador.group("TEXTO") != null ? "T-TEXTO"
								: comparador.group("IDENTIFICADOR") != null ? "IDENTIFICADOR"
								: comparador.group("PARENTESIS") != null ? "PARENTESIS"
								: comparador.group("OPERADOR") != null ? "OPERADOR"
								: comparador.group("ESPACIO") != null ? "ESPACIO"
								: comparador.group("COMA") != null ? "COMA"
								: null;
//DATO RECONOCIDO:
//			errores.add(tipoDeDato + " : -" + comparador.group() + "- estado anterior: "+estado);
			
			/**
			 * ESTADO:
			 * 	   -1 es coma
			 * 		1 es dato simple o variable/identificador
			 * 		2 es operador
			 * 		3 es parentesis de apertura
			 * 		4 es parentesis de cierre
			 * */
			
			
			contador++;
			if (tipoDeDato.equals("ESPACIO")) {
				contador--;
			} else if (tipoDeDato.equals("COMA")) {
				if (compuesta && declaracionAsignacion) {
					if (estado==1) {
						separado.add(comparador.group());
						contador--;
						estado = -1;
					}else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": La <,> (COMA) no se encuentra despues\n de un identificador").toString());
						return null;
					}
				} else {
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Separador <,> (COMA) no valido para\neste campo").toString());
					return null;
				}
			} else if (tipoDeDato.equals("PARENTESIS")) {
				
				if (declaracionAsignacion) {
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Parentesis no valido en una declaracion").toString());
					return null;
				}
				
				if (comparador.group().equals("(")) {
					//errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Parentesis de apertura estado:").append(estado).toString());
					if (estado != 1) {
						if (estado !=4) {
							errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Parentesis de apertura").toString());
							separado.add(comparador.group());
							parentesis++;
							estado = 3;
						}else {
							errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": No hay operador entre el parentesis de cierre y el parentesis de apertura").toString());
							return null;
						}
					} else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Parentesis de apertura posterior a tipo de dato").toString());
						return null;
					}
				} else if (comparador.group().equals(")")) {
					if (parentesis > 0) {
						if (estado!=0) {
							if (estado != 2) {
								if (estado != 3) {
									errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Parentesis de cierre").toString());
									parentesis--;
									estado = 4;
									separado.add(comparador.group());
								}else {
									errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Expresion vacia entre parentesis").toString());
									return null;
								}
							}else {
								errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": No hay identificador antes del parentesis de cierre").toString());
								return null;
							}
						}else {
							errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Parentesis de cierre al inicio de expresion").toString());
							return null;
						}
					}else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Parentesis de cierre sin parentesis de apertura presente").toString());
						return null;
					}
				}
			} else if (tipoDeDato.equals("IDENTIFICADOR") && estado != 1) {
				String llave = tablaDeSimbolos.hash(comparador.group());
				boolean estaEnTabla = tablaDeSimbolos.containsKey(llave);
				
				if (declaracionAsignacion) {
					if (comparador.group().length() <= 15) {
						if (!comparador.group().matches(PALABRAS_PROHIBIDAS)) {
							if (!estaEnTabla) {
								tablaDeSimbolos.put(llave, new Variable("", tipoDeVariable, comparador.group()));
								errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Se añadio el identificador en la tabla\nde simbolos <").append(llave).append(":").append(tipoDeVariable).append(">").toString());
								estado = 1;
								separado.add(comparador.group());
							
							} else {
								errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": El identificador ya fue declarado").toString());
								return null;
							}
						}else {
							errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": El nombre de este identificador <").append(comparador.group()).append(">\nes una palabra reservada").toString());
							return null;
						}
					} else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": El identificador es mayor a 15 caracteres").toString());
						return null;
					}

				} else if (estaEnTabla) {
					errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Se encontro el identificador <").append(comparador.group()).append("> en la tabla").toString());
					estado = 1;
					separado.add(comparador.group());

				} else {
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": No se encontró el identificador <").append(comparador.group()).append("> en la tabla").toString());
					return null;
				}
			} else if (tipoDeDato.equals("OPERADOR") && estado != 2) {
//				errores.add("DEBUG L" + linea + ": " + contador + " " + comparador.group() + " "
//						+ comparador.group().equals("-"));
				if (declaracionAsignacion) {
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Operador no valido en declaración").toString());
					return null;
				}
				
				if (comparador.group().equals("-")) {
					if (estado==3 || (contador==1 && estado == 0) ) {
						errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Operador negativo inicial").toString());
						estado = 2;
						contador--;
						separado.add("0");
						separado.add(comparador.group());
	
					}else if (estado == 1 || estado==4) {
						estado = 2;
						separado.add(comparador.group());
					} else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Operador negativo no precede a un parentesis de apertura,\n no es inicial en la expresion y tampoco precede un identificador o valor").toString());
						return null;
					}
				} else if (contador > 1 ) {
					if (estado!=2) {
						if (estado!=3) {
							errores.add((new StringBuilder()).append("INFO L").append(linea).append(": Operador no inicial").toString());
							estado = 2;
							separado.add(comparador.group());
						}else {
							errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": El operador precede un parentesis de apertura").toString());
							return null;
						}
						
					}else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": El operador precede un operador").toString());
						return null;
					}
					
				} else {
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": El operador es inicial en la expresion").toString());
					return null;
				}
			} else if (tipoDeDato.substring(0, 1).equals("T") ) {	
				// TIPO DE DATO RECONOCIDO
				//errores.add("INFO L" + linea + ": TIPO DE DATO RECONOCIDO");
				if (!declaracionAsignacion) {
					if (estado!=1) {
						if (estado != 4) {
							estado = 1;
							separado.add(comparador.group());
						}else {
							errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Dato simple precede un parentesis de cierre").toString());
							return null;
						}
					}else {
						errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Dato simple precede un identificador").toString());
						return null;
					}
				} else{
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Dato simple no valido en una declaración").toString());
					return null;
				}
				
			} else {
				errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Error no identificado (multiple) ").append(comparador.group()).toString());
				return null;
			}

			if (contador == 0|| contador==1) {
				if (comparador.start() == 0) {
					ultimoCaracterIdentificado = comparador.end();
				} else if(ultimoCaracterIdentificado!=comparador.start()){
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Simbolo no identificado <").append(expresion.substring(ultimoCaracterIdentificado, comparador.start())).append(">").toString());
					return null;
				}
			}

			if (comparador.start() != 0) {
				if (ultimoCaracterIdentificado != comparador.start()) {
					errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Simbolo no identificado <").append(expresion.substring(ultimoCaracterIdentificado, comparador.start())).append(">").toString());
					return null;
				} else {

					ultimoCaracterIdentificado = comparador.end();
				}
			}

			if (!compuesta && contador > 1) {
				errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Expresion compuesta en campo simple").toString());
				return null;
			}
		}

		if (estado == 2) {
			errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Termina en operador").toString());
			return null;
		}
		
		if (estado == -1) {
			errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Termina en coma").toString());
			return null;
		}

		if (parentesis != 0) {
			errores.add((new StringBuilder()).append("ERROR L").append(linea).append(": Error en uso de parentesis").toString());
			return null;
		}

		return separado;
	}


	public String imprimirTablaDeSimbolos() {
		StringBuilder tablaDeSimbolosTexto = new StringBuilder();
		tablaDeSimbolosTexto.append("--------------- TABLA DE SIMBOLOS ---------------\n");
		ArrayList<String> variables = tablaDeSimbolos.elements();
		for (int i = 0; i < variables.size(); i++) {
			tablaDeSimbolosTexto.append(variables.get(i)).append("\n");
		}
		return tablaDeSimbolosTexto.toString();
	}
	
	public String imprimirExpresionesPosfijas() {
		StringBuilder expresionesPosfijasTexto = new StringBuilder();
		expresionesPosfijasTexto.append("--------------- EXPRESIONES POSFIJAS ---------------\n");
		expresionesPosfijasTexto.append(expresionesPosfijas);
		return expresionesPosfijasTexto.toString();
	}

	public ArrayList<String> getVariables() {
		return tablaDeSimbolos.elements();
	}

	public void inicializarListaDeErrores() {
		tablaDeSimbolos = new TablaDeSimbolos();
		errores = new ArrayList<String>();
		expresionesPosfijas = new Hashtable<Integer,ExpresionesLinea >();
	}

	public void añadirVariable(String nombre, String tipo) {
		tablaDeSimbolos.put(tablaDeSimbolos.hash(nombre), new Variable("", tipo, nombre));
	}

	public void actualizarVariable(String nombre, String valor) {
		tablaDeSimbolos.get(tablaDeSimbolos.hash(nombre)).valor = valor;
	}

	public TablaDeSimbolos getTablaDeSimbolos() {
		return tablaDeSimbolos;
	}

	public ArrayList<String> presentarErrores() {
		return errores;
	}

	public String encontrarTipoParaIdentificador(String identificador) {
		if (tablaDeSimbolos.containsKey(tablaDeSimbolos.hash(identificador))) {
			return tablaDeSimbolos.get(tablaDeSimbolos.hash(identificador)).getTipo();
		} else {
			errores.add("ERROR: No se encuentra el identificador\n <" + identificador + ">");
			return "ERROR";
		}
	}
	
	public void añadirExpresionLinea(String expresion, int linea, String tipo) {
//		System.out.println("Se añade: "+expresion);
		if (expresionesPosfijas.containsKey(linea)) {
			expresionesPosfijas.get(linea).setSegundaParte(expresion);
//			System.out.println("Se añade primera parte: "+expresion);
		} else {
			expresionesPosfijas.put(linea, new ExpresionesLinea(expresion));
//			System.out.println("Se añade segunda parte: "+expresion);
		}
		
		if (tipo!=null) {
//			System.out.println("Se añade tipo: "+expresion);
			expresionesPosfijas.get(linea).setTipo(tipo);
		}
	}
	
	public Hashtable<Integer, ExpresionesLinea> getExpresionesPosfijas() {
		return expresionesPosfijas;
	}

	public void setExpresionesPosfijas(Hashtable<Integer, ExpresionesLinea> expresionesPosfijas) {
		this.expresionesPosfijas = expresionesPosfijas;
	}
	
}
