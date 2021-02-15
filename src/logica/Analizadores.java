package logica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Analizadores {
	private static final String ENTERO = "[0-9]+";
	private static final String DECIMAL = "[0-9]+\\.[0-9]+";
	private static final String LOGICO = "true|false";
	private static final String TEXTO = "\"([^\"\\\\]|\\\\.)*\"";
	//private static final String TEXTO ="\".*\"";
	private static final String PARENTESIS = "\\(|\\)";
	private static final String OPERADOR = "\\^|\\/|\\*|\\+|\\-|\\%|\\=\\=|\\=!|\\>|\\<|\\<\\=|\\>\\=|\\&\\&|\\|\\|";
	
	/** IDENTIFICADOR DE ESPACIOS PARA TOAMR SIMBOLOS NO RECONOCIDOS */
	private static final String ESPACIO = "\\s+";
	
	/** EXPRESION REGULAR PARA METODO DE LECTURA */
	private static final String LEER = "Leer["+ESPACIO+TEXTO+ESPACIO+"]";

	private static final String IDENTIFICADOR = "[a-z]+([0-9]+)?";

	private Pattern PATRON = Pattern.compile(
			   "(?<LOGICO>" + LOGICO + ")" 
			+ "|(?<DECIMAL>" + DECIMAL + ")"
			+ "|(?<ENTERO>" + ENTERO + ")" 
			+ "|(?<IDENTIFICADOR>" + IDENTIFICADOR + ")" 
			+ "|(?<TEXTO>" + TEXTO + ")"
			+ "|(?<PARENTESIS>" + PARENTESIS + ")" 
			+ "|(?<OPERADOR>" + OPERADOR + ")"
			+ "|(?<ESPACIO>" + ESPACIO + ")" 
			);

	private ArrayList<String> errores;
	TablaDeSimbolos tablaDeSimbolos;
	
	public Analizadores() {
		errores = new ArrayList<>();
		tablaDeSimbolos= new TablaDeSimbolos();
	}

	public boolean evaluate(boolean compuesta, boolean declaracion, String tipoDeVariable, String expresion, int linea) {
		String pf = analizador(compuesta, declaracion, tipoDeVariable, expresion, linea);
		errores.add(" POST FIJO --------> "+pf);
		if (pf.isEmpty()) {
			return false;
		}
		if (compuesta) {
			String rta = evaluar(pf);
			if (!rta.equals("R")) {
				errores.add("--- Se espera el tipo de dato: " + tipoDeVariable +" | Tipo generado: " + rta);
				if (rta.equals(tipoDeVariable) ) {
					return true;
				} else {
					errores.add("ERROR: EL RESULTADO DE LA EXPRESION NO COINCIDE \n CON EL TIPO DE VARIABLE ");
					return false;
				}
			} else {
				errores.add("ERROR: Expresion no operable");
				return false;
			}
		}
		return true;
	}

	public String analizador(boolean compuesta, boolean declaracion, String tipoDeVariable, String expresion, int linea) {
		Matcher comparador = PATRON.matcher(expresion);
		int contador = 0, estado = 0, parentesis = 0, ultimoCaracterIdentificado=0;
		boolean subExpresion = false;
		String postFijo = "", subExpresionS = "";

		Stack<String> pila = new Stack<String>();

		while (comparador.find()) {
			
			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
					: comparador.group("DECIMAL") != null ? "T-DECIMAL"
					: comparador.group("LOGICO") != null ? "T-LOGICO"
					: comparador.group("TEXTO") != null ? "T-TEXTO"
					: comparador.group("IDENTIFICADOR") != null ? "IDENTIFICADOR"
					: comparador.group("PARENTESIS") != null ? "PARENTESIS"
					: comparador.group("OPERADOR") != null ? "OPERADOR" 
					: comparador.group("ESPACIO") != null ? "ESPACIO"
							: null;

			errores.add(tipoDeDato+ " : " + comparador.group() + " INICIO: "+ comparador.start()+ " FINAL: "+comparador.end());
			contador++;
			if(tipoDeDato.equals("ESPACIO")) {
				contador--;
			}else if (tipoDeDato.equals("PARENTESIS")) {
				
				if (comparador.group().equals("(")) {
					if (subExpresion) {
						subExpresionS = subExpresionS + " " + comparador.group();
					}
					errores.add("INFO L" + linea + ": PARENTESIS DE APERTURA E:" + estado);
					if (estado == 0 || estado == 2) {
						errores.add("INFO L" + linea + ": PARENTESIS DE APERTURA");
						subExpresion = true;
						parentesis++;
					} else {
						errores.add("INFO L" + linea + ": PARENTESIS DE APERTURA POSTERIOR A TIPO DE DATO");
						return "";
					}

				} else if (comparador.group().equals(")")) {
					if ((estado != 2 && estado != 0) && parentesis > 0) {

						errores.add("INFO L" + linea + ": PARENTESIS DE CIERRE");
						parentesis--;
						if (parentesis == 0) {
							errores.add("SUBEXPRESION - " + subExpresionS);
							String recursivaPF = analizador(compuesta, declaracion, tipoDeVariable, subExpresionS,
									linea);
							if (recursivaPF.isEmpty()) {
								errores.add("********recursiva es vacia********");
								return "";
							} else {
								postFijo = postFijo + " " + recursivaPF;
								subExpresion = false;
								subExpresionS = "";
							}
						}
						if (subExpresion) {
							subExpresionS = subExpresionS + " " + comparador.group();
						}
					} else {
						errores.add("ERROR L" + linea + ": PARENTESIS DE CIERRE EN INICIO DE EXPRESION");
						return "";
					}

				}

			} else if (tipoDeDato.equals("IDENTIFICADOR") && estado != 1) {
				
				String llave = hash(comparador.group());
				boolean estaEnTabla = tablaDeSimbolos.containsKey(llave);
				if (declaracion) {
					if (!estaEnTabla) {
						tablaDeSimbolos.put(llave, new Variable(null, tipoDeVariable, comparador.group()));
						errores.add("INFO L" + linea + ": SE AÑADIO EL IDENTIFICADOR A LA TABLA DE SIMBOLOS <"+tipoDeVariable+">");
						estado = 1;
						if (subExpresion) {
							subExpresionS = subExpresionS + " " + comparador.group();
						} else {
							postFijo = postFijo + " " + comparador.group();
						}
					} else {
						errores.add("ERROR L" + linea + ": IDENTIFICADOR DUPLICADO EN LA TABLA");
						return "";
					}
				} else if (estaEnTabla) {
					errores.add("INFO L" + linea + ": ENCONTRO EL IDENTIFICADOR EN LA TABLA");
					estado = 1;
					if (subExpresion) {
						subExpresionS = subExpresionS + " " + comparador.group();
					} else {
						postFijo = postFijo + " " + comparador.group();
					}
				} else {
					errores.add("ERROR L" + linea + ": NO SE ENCONTRO EL IDENTIFICADOR EN LA TABLA");
					return "";
				}
			} else if (tipoDeDato.equals("OPERADOR") && estado != 2) {
				errores.add("DEBUG L" + linea + ": "+ contador+" "+comparador.group()+ " "+comparador.group().equals("-"));
				if (contador == 1 && comparador.group().equals("-")) {
					errores.add("INFO L" + linea + ": OPERADOR NEGATIVO INICIAL");
					estado = 2;
					if (subExpresion) {
						subExpresionS = subExpresionS + " " + comparador.group();
					} else if (pila.empty()) {
						pila.push("-");
					}
				} else if (contador != 0 && estado == 1) {

					errores.add("INFO L" + linea + ": OPERADOR NO INICIAL");
					estado = 2;
					if (subExpresion) {
						subExpresionS = subExpresionS + " " + comparador.group();
					} else if (pila.empty()) {
						pila.push(comparador.group());
					} else {
						int actual = jerarquia(comparador.group());
						int ultimoPila = jerarquia(pila.peek());

						while (actual < ultimoPila) {
							postFijo = postFijo + " " + pila.peek();
							pila.pop();
							ultimoPila = !pila.empty() ? jerarquia(pila.peek()) : actual;
						}

						if (actual == ultimoPila) {
							while (!pila.empty()) {
								postFijo = postFijo + " " + pila.peek();
								pila.pop();
							}
							pila.push(comparador.group());
						} else if (actual > ultimoPila) {
							pila.push(comparador.group());
						}
					}
				} else {
					errores.add("ERROR L" + linea + ": OPERADOR INICIAL");
					return "";
				}
			} else if (tipoDeDato.substring(0, 1).equals("T") && declaracion == false && estado != 1) {
				
				errores.add("INFO L" + linea + ": TIPO DE DATO RECONOCIDO");
				estado = 1;
				if (subExpresion) {
					subExpresionS = subExpresionS + " " + comparador.group();
				} else {
					postFijo = postFijo + " " + comparador.group();
				}
			}else{
				errores.add("ERROR L" + linea + ": MULTIPLE | ES UN IDENTIFICADOR NO VALIDO \n" + comparador.group());
				return "";
			}
			
			if( contador == 0) {
				if(comparador.start()== 0 ) {
					ultimoCaracterIdentificado=comparador.end();
				} else {
					errores.add("ERROR L" + linea + ": SIMBOLO NO IDENTIFICADO <"+ultimoCaracterIdentificado+","+comparador.start() +">");
				}
			}

			if(comparador.start() != 0) {
				errores.add("INFO L" + linea + ": SIMBOLO "+comparador.group()+" <"+ultimoCaracterIdentificado+","+comparador.start() +">");
				if(ultimoCaracterIdentificado != comparador.start() ) {
					errores.add("ERROR L" + linea + ": SIMBOLO NO IDENTIFICADO <"+expresion.substring(ultimoCaracterIdentificado, comparador.start())+">");
					return "";
				}else {
					
					ultimoCaracterIdentificado = comparador.end();
				}
			}
			
			if (!compuesta && contador > 1) {
				errores.add("ERROR L" + linea + ": EXPRESION COMPUESTA EN CAMPO SIMPLE");
				return "";
			}

		}

		if (estado == 2) {
			errores.add("ERROR L" + linea + ": TERMINA EN OPERADOR");
			return "";
		}

		if (parentesis != 0) {
			errores.add("ERROR L" + linea + ": REVISAR PARENTESIS");
			return "";
		}

		while (!pila.empty()) {
			postFijo = postFijo + " " + pila.peek();
			pila.pop();
		}

		return postFijo;
	}

	public int jerarquia(String operador) {
		int rta = (operador.matches("\\|\\|")) ? 1
				: (operador.matches("\\&\\&")) ? 2
				: (operador.matches("\\=\\=|\\=!|\\>|\\<|\\<\\=|\\>\\=")) ? 3
				: (operador.matches("\\%")) ? 4
				: (operador.matches("\\+|\\-")) ? 5
				: (operador.matches("\\*|\\/")) ? 6
				: (operador.matches("\\^")) ? 7
				: (operador.matches("\\(|\\)")) ? 8 : 0;
		return rta;
	}

	public String evaluar(String expresion) {
		Matcher comparador = PATRON.matcher(expresion);
		Stack<String> lista = new Stack<String>();
		String tipoDeDato;
		String vec= "R";
		
		while (comparador.find()) {
			tipoDeDato = comparador.group("DECIMAL") != null ? "D"
						: comparador.group("ENTERO") != null ? "E"
						: comparador.group("LOGICO") != null ? "L"
						: comparador.group("TEXTO") != null ?  "T"
						: comparador.group("IDENTIFICADOR") != null ? "I" 
						: comparador.group("OPERADOR") != null ? "O"      
						: comparador.group("ESPACIO") != null ? "S" : "R"; 
			if(!tipoDeDato.equals("S")) {
				if (!tipoDeDato.equals("O")) {
					if (tipoDeDato.equals("I")) {
						lista.push(tablaDeSimbolos.get(hash(comparador.group())).getTipo());
					} else if (tipoDeDato.equals("T")) {
						String textoSinComillas = comparador.group().replaceAll("\"","");
						lista.push(tipoDeDato);
					} else {
						lista.push( tipoDeDato);
					}
				} else if (lista.size() > 1) {
					String operando2Tipo = lista.peek();
					lista.pop();
					String operando1Tipo = lista.peek();
					lista.pop();
					//errores.add(operando1.getValor() + " " + comparador.group() + " " + operando2.getValor());
					String resultado = operar( operando1Tipo, comparador.group(), operando2Tipo);
					if (resultado.equals("R")) {
						return vec;
					}
					lista.push(resultado);
				} 
				//---- NEGATIVO 
				//else {
					//Variable a = lista.peek();
					//a.setValor(comparador.group() + a.getValor());
					
				//}
			}
		}
		vec = lista.peek();
		return  vec;
	}

	public String operar( String tipo1, String operador,  String tipo2) {
		
		/************************ OPERANDO *************************************/
		String vec = "R";
		if (tipo1.equals("D") && tipo2.equals("E") ) {
			if (operador.equals("^")) {
				vec = "D";
				return vec;
			} else if (operador.equals("/")) {
				vec = "D";
				return vec;
			} else if (operador.equals("*")) {
				vec = "D";
				return vec;
			} else if (operador.equals("+")) {
				vec = "D";
				return vec;
			} else if (operador.equals("-")) {
				vec = "D";
				return vec;
			} else if (operador.equals("%")) {
				vec = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec = "L";
				return vec;
			}
			/*************************************************************/
		} else if (tipo1.equals("E") && tipo2.equals("D")) {
			if (operador.equals("^")) {
				vec = "D";
				return vec;
			} else if (operador.equals("/")) {
				vec = "D";
				return vec;
			} else if (operador.equals("*")) {
				vec = "D";
				return vec;
			} else if (operador.equals("+")) {
				vec = "D";
				return vec;
			} else if (operador.equals("-")) {
				vec = "D";
				return vec;
			} else if (operador.equals("%")) {
				vec = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("D") && tipo2.equals("T")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("D")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("E") && tipo2.equals("T")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("E")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("L") && tipo2.equals("T")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("L")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("D") && tipo2.equals("D")) {
			if (operador.equals("^")) {
				vec = "D";
				return vec;
			} else if (operador.equals("/")) {
				vec = "D";
				return vec;
			} else if (operador.equals("*")) {
				vec = "D";
				return vec;
			} else if (operador.equals("+")) {
				vec = "D";
				return vec;
			} else if (operador.equals("-")) {
				vec = "D";
				return vec;
			} else if (operador.equals("%")) {
				// VERIFICAR
				vec = "E";
				return vec;
				// VERIFICAR
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("T")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("E") && tipo2.equals("E")) {
			if (operador.equals("^")) {
				vec = "E";
				return vec;
			} else if (operador.equals("/")) {
				vec = "E";
				return vec;
			} else if (operador.equals("*")) {
				vec = "E";
				return vec;
			} else if (operador.equals("+")) {
				vec = "E";
				return vec;
			} else if (operador.equals("-")) {
				vec = "E";
				return vec;
			} else if (operador.equals("%")) {
				vec = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("L") && tipo2.equals("L")) {
			if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec = "L";
				return vec;
			} else if (operador.equals("&&")) {
				vec = "L";
				return vec;
			} else if (operador.equals("||")) {
				vec = "L";
				return vec;
			}

		}
		
		vec = "R";

		return vec;
	}

	public void imprimirTablaDeSimbolos() {
		System.out.println("--------------- TABLA DE SIMBOLOS ---------------");
		Enumeration<Variable> values = tablaDeSimbolos.elements();
		while (values.hasMoreElements()) {
			Variable aux = values.nextElement();
			System.out.println(aux.getTipo() + " " + aux.getValor() + " " + aux.getNombre());
		}
	}

	public ArrayList<String> getVariables() {
		Enumeration<Variable> values = tablaDeSimbolos.elements();
		ArrayList<String> variables = new ArrayList<String>();
		while (values.hasMoreElements()) {
			Variable aux = values.nextElement();
			variables.add(aux.getNombre());
//			+ "\n" + aux.getValor());
		}
		return variables;
	}

	public String hash(String identificacdor) {
		String llave = "";
		int tam = identificacdor.length();
		for (int i = 0; i < tam; i++) {
			llave += (int)identificacdor.charAt(i);
		}
		return llave;
	}

	public void inicializarListaDeErrores() {
		//tablaDeSimbolos = new Hashtable<String, Variable>();
		tablaDeSimbolos= new TablaDeSimbolos();
		errores = new ArrayList<String>();
	}

	public void añadirVariable(String nombre, String tipo) {
		tablaDeSimbolos.put(hash(nombre), new Variable(null, tipo, nombre));
	}
	
	public void actualizarVariable(String nombre, String valor) {
		tablaDeSimbolos.get(tablaDeSimbolos.hash(nombre)).valor=valor;
	}
	
	public TablaDeSimbolos getTablaDeSimbolos() {
		return tablaDeSimbolos;
	}

	public String presentarErrores() {
		String erroresResultado = "";
		int tamaño = errores.size();
		for (int i = 0; i < tamaño; i++) {
			erroresResultado += errores.get(i) + "\n";
		}
		return erroresResultado;
	}

	public String encontrarTipoParaIdentificador(String identificador) {
		if (tablaDeSimbolos.containsKey(hash(identificador))) {
			return tablaDeSimbolos.get(hash(identificador)).getTipo();
		}
		return "R";
	}

}
