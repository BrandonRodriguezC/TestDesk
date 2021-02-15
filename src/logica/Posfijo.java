package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Posfijo {
	static final String ENTERO = "[0-9]+";
	static final String DECIMAL = "[0-9]+\\.[0-9]+";
	static final String LOGICO = "true|false";
	static final String TEXTO = "\"([^\"\\\\]|\\\\.)*\"";
	static final String PARENTESIS = "\\(|\\)";
	static final String OPERADOR = "\\^|\\/|\\*|\\+|\\-|\\%|\\=\\=|\\=!|\\>\\=|\\<\\=|\\<|\\>|\\&\\&|\\|\\|";

	// private static final String COMENTARIO = "//[^\n]*" + "|" +
	// "/\\*(.|\\R)*?\\*/";

	static final String IDENTIFICADOR = "[a-z]+[0-9]+|[a-z]+";

	static Pattern PATRON = Pattern.compile("(?<LOGICO>" + LOGICO + ")" + "|(?<DECIMAL>" + DECIMAL + ")" + "|(?<ENTERO>"
			+ ENTERO + ")" + "|(?<IDENTIFICADOR>" + IDENTIFICADOR + ")" + "|(?<TEXTO>" + TEXTO + ")" + "|(?<PARENTESIS>"
			+ PARENTESIS + ")" + "|(?<OPERADOR>" + OPERADOR + ")");

	public static String [] separar( String expresion) {
		Matcher comparador = PATRON.matcher(expresion);
		
		List<String> separado = new ArrayList<String>();

		while (comparador.find()) {

			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
					: comparador.group("DECIMAL") != null ? "T-DECIMAL"
							: comparador.group("LOGICO") != null ? "T-LOGICO"
									: comparador.group("TEXTO") != null ? "T-TEXTO"
											: comparador.group("IDENTIFICADOR") != null ? "IDENTIFICADOR"
													: comparador.group("PARENTESIS") != null ? "PARENTESIS"
															: comparador.group("OPERADOR") != null ? "OPERADOR" : null;

			if (tipoDeDato.equals("PARENTESIS")) {
				separado.add(comparador.group());
			} else if (tipoDeDato.equals("IDENTIFICADOR")) {
				separado.add(comparador.group());
			} else if (tipoDeDato.equals("OPERADOR")) {
				separado.add(comparador.group());
			} else if (tipoDeDato.substring(0, 1).equals("T")) {
				separado.add(comparador.group());
			} 
		}
		String expresionVec[]= new String [separado.size()];
		expresionVec = separado.toArray(expresionVec); 
		
		return expresionVec;
	}

	
	public static String postfija(String[] expresionVec) {
		String resultado = "", subexpresion = "";
		
		int i = 0, izPa = 0, dePa = 0;
		
		Stack <String>pila = new Stack <String>();
		
		while (i < expresionVec.length) {
			
			if (expresionVec[i].equals("(")) {
				do {
					subexpresion = subexpresion + expresionVec[i];
					if (expresionVec[i].equals("("))
						izPa++;
					else if (expresionVec[i].equals(")"))
						dePa++;
					i++;
				} while (izPa != dePa);
				
				subexpresion = subexpresion.substring(1, subexpresion.length() - 1);
				resultado = resultado + " " + postfija(separar(subexpresion));
				
			} else if (esDato(expresionVec[i])) {
				resultado = resultado + " " + expresionVec[i];
				i++;
			} else if (pila.empty()) {
				pila.push(expresionVec[i]);
				i++;
			} else {
				if (jerarquia(expresionVec[i]) < jerarquia(pila.peek())) {
					resultado = resultado + " " + pila.peek();
					pila.pop();
				} else if (jerarquia(pila.peek()) == jerarquia(expresionVec[i])) {
					while (!pila.empty()) {
						resultado = resultado + " " + pila.peek();
						pila.pop();
					}
					pila.push(expresionVec[i]);
					i++;
				} else if (jerarquia(pila.peek()) < jerarquia(expresionVec[i])) {
					pila.push(expresionVec[i]);
					i++;
				}
			}
		}
		if (!pila.empty()) {
			while (!pila.empty()) {
				resultado = resultado + " " + pila.peek();
				pila.pop();
			}
		}
		resultado = resultado.substring(1, resultado.length());
		return resultado;
	}
	
	
	public static boolean esDato(String identificador) {
		Matcher comparador = PATRON.matcher(identificador);
		if (comparador.find()) {
			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
					: comparador.group("DECIMAL") != null ? "T-DECIMAL"
					: comparador.group("LOGICO") != null ? "T-LOGICO"
					: comparador.group("TEXTO") != null ? "T-TEXTO"
					: comparador.group("IDENTIFICADOR") != null ? "IDENTIFICADOR"
					: comparador.group("PARENTESIS") != null ? "PARENTESIS"
					: comparador.group("OPERADOR") != null ? "OPERADOR" : null;
			if(!tipoDeDato.equals("PARENTESIS") && !tipoDeDato.equals("OPERADOR")) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
//	public static String analizador(String expresion) {
//		Matcher comparador = PATRON.matcher(expresion);
//
//		int contador = 0, estado = 0, parentesis = 0;
//
//		boolean subExpresion = false;
//
//		String postFijo = "", subExpresionS = "";
//
//		Stack<String> pila = new Stack<String>();
//
//		while (comparador.find()) {
//
//			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
//					: comparador.group("DECIMAL") != null ? "T-DECIMAL"
//							: comparador.group("LOGICO") != null ? "T-LOGICO"
//									: comparador.group("TEXTO") != null ? "T-TEXTO"
//											: comparador.group("IDENTIFICADOR") != null ? "IDENTIFICADOR"
//													: comparador.group("PARENTESIS") != null ? "PARENTESIS"
//															: comparador.group("OPERADOR") != null ? "OPERADOR" : null;
//
//			if (tipoDeDato.equals("PARENTESIS")) {
//				if (comparador.group().equals("(")) {
//					if (subExpresion) {
//						subExpresionS = subExpresionS + " " + comparador.group();
//					}
//					System.out.println("INFO L" + linea + ": PARENTESIS DE APERTURA E:" + estado);
//					if (estado == 0 || estado == 2) {
//						System.out.println("INFO L" + linea + ": PARENTESIS DE APERTURA");
//						subExpresion = true;
//						parentesis++;
//					} else {
//						System.out.println("INFO L" + linea + ": PARENTESIS DE APERTURA POSTERIOR A TIPO DE DATO");
//						return "";
//					}
//
//				} else if (comparador.group().equals(")")) {
//					if ((estado != 2 && estado != 0) && parentesis > 0) {
//
//						System.out.println("INFO L" + linea + ": PARENTESIS DE CIERRE");
//						parentesis--;
//						if (parentesis == 0) {
//							System.out.println("SUBEXPRESION - " + subExpresionS);
//							String recursivaPF = analizador(compuesta, declaracion, tipoDeVariable, subExpresionS,
//									linea, ts);
//							if (recursivaPF.isEmpty()) {
//								System.out.println("********recursiva es vacia********");
//								return "";
//							} else {
//								postFijo = postFijo + " " + recursivaPF;
//								subExpresion = false;
//								subExpresionS = "";
//							}
//						}
//						if (subExpresion) {
//							subExpresionS = subExpresionS + " " + comparador.group();
//						}
//					} else {
//						System.out.println("ERROR L" + linea + ": PARENTESIS DE CIERRE EN INICIO DE EXPRESION");
//						return "";
//					}
//
//				}
//
//			} else if (tipoDeDato.equals("IDENTIFICADOR") && estado != 1) {
//				int llave = ts.hash(comparador.group());
//				boolean estaEnTabla = ts.containsKey(llave);
//				if (declaracion) {
//					if (!estaEnTabla) {
//						ts.put(llave, new Variable(tipoDeVariable, comparador.group()));
//						System.out.println("INFO L" + linea + ": SE AÑADIO EL IDENTIFICADOR A LA TABLA DE SIMBOLOS");
//						estado = 1;
//						if (subExpresion) {
//							subExpresionS = subExpresionS + " " + comparador.group();
//						} else {
//							postFijo = postFijo + " " + comparador.group();
//						}
//					} else {
//						System.out.println("ERROR L" + linea + ": IDENTIFICADOR DUPLICADO EN LA TABLA");
//						return "";
//					}
//				} else if (estaEnTabla) {
//					System.out.println("INFO L" + linea + ": ENCONTRO EL IDENTIFICADOR EN LA TABLA");
//					estado = 1;
//					if (subExpresion) {
//						subExpresionS = subExpresionS + " " + comparador.group();
//					} else {
//						postFijo = postFijo + " " + comparador.group();
//					}
//				} else {
//					System.out.println("ERROR L" + linea + ": NO SE ENCONTRO EL IDENTIFICADOR EN LA TABLA");
//					return "";
//				}
//			} else if (tipoDeDato.equals("OPERADOR") && estado != 2) {
//
//				if (contador == 0 && comparador.group().equals("-")) {
//					System.out.println("INFO L" + linea + ": OPERADOR NEGATIVO INICIAL");
//					estado = 2;
//					if (subExpresion) {
//						subExpresionS = subExpresionS + " " + comparador.group();
//					} else if (pila.empty()) {
//						pila.push("-");
//					}
//				} else if (contador != 0 && estado == 1) {
//
//					System.out.println("INFO L" + linea + ": OPERADOR NO INICIAL");
//					estado = 2;
//					if (subExpresion) {
//						subExpresionS = subExpresionS + " " + comparador.group();
//					} else if (pila.empty()) {
//						pila.push(comparador.group());
//					} else {
//						int actual = jerarquia(comparador.group());
//						int ultimoPila = jerarquia(pila.peek());
//
//						while (actual < ultimoPila) {
//							postFijo = postFijo + " " + pila.peek();
//							pila.pop();
//							ultimoPila = !pila.empty() ? jerarquia(pila.peek()) : actual;
//						}
//
//						if (actual == ultimoPila) {
//							while (!pila.empty()) {
//								postFijo = postFijo + " " + pila.peek();
//								pila.pop();
//							}
//							pila.push(comparador.group());
//						} else if (actual > ultimoPila) {
//							pila.push(comparador.group());
//						}
//					}
//				} else {
//					System.out.println("ERROR L" + linea + ": OPERADOR INICIAL");
//					return "";
//				}
//			} else if (tipoDeDato.substring(0, 1).equals("T") && declaracion == false && estado != 1) {
//				System.out.println("INFO L" + linea + ": TIPO DE DATO RECONOCIDO");
//				estado = 1;
//				if (subExpresion) {
//					subExpresionS = subExpresionS + " " + comparador.group();
//				} else {
//					postFijo = postFijo + " " + comparador.group();
//				}
//			} else {
//				System.out.println(
//						"ERROR L" + linea + ": MULTIPLE | ES UN IDENTIFICADOR NO VALIDO \n" + comparador.group());
//				return "";
//			}
//
//			contador++;
//			if (!compuesta && contador > 1) {
//				System.out.println("ERROR L" + linea + ": EXPRESION COMPUESTA EN CAMPO SIMPLE");
//				return "";
//			}
//
//		}
//
//		if (estado == 2) {
//			System.out.println("ERROR L" + linea + ": TERMINA EN OPERADOR");
//			return "";
//		}
//
//		if (parentesis != 0) {
//			System.out.println("ERROR L" + linea + ": REVISAR PARENTESIS");
//			return "";
//		}
//
//		while (!pila.empty()) {
//			postFijo = postFijo + " " + pila.peek();
//			pila.pop();
//		}
//
//		return postFijo;
//	}

	public static int jerarquia(String operador) {

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
}
