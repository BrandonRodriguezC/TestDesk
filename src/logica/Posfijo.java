package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Posfijo {
	static final String ENTERO = "[0-9]+";
	static final String REAL = "[0-9]+\\.[0-9]+";
	/** REVISAR OPERADOR LOGICO (VERDADERO - FALSO)*/
	static final String LOGICO = "true|false";
	static final String TEXTO = "\"([^\"\\\\]|\\\\.)*\"";
	static final String PARENTESIS = "\\(|\\)";
	/** REVISAR =! */
	static final String OPERADOR = "\\^|\\/|\\*|\\+|\\-|\\%|\\=\\=|\\!\\=|\\>\\=|\\<\\=|\\<|\\>|\\&\\&|\\|\\|";

	// private static final String COMENTARIO = "//[^\n]*" + "|" +
	// "/\\*(.|\\R)*?\\*/";

	static final String IDENTIFICADOR = "[a-z]+[0-9]+|[a-z]+";

	static Pattern PATRON = Pattern.compile("(?<LOGICO>" + LOGICO + ")" + "|(?<REAL>" + REAL + ")" + "|(?<ENTERO>"
			+ ENTERO + ")" + "|(?<IDENTIFICADOR>" + IDENTIFICADOR + ")" + "|(?<TEXTO>" + TEXTO + ")" + "|(?<PARENTESIS>"
			+ PARENTESIS + ")" + "|(?<OPERADOR>" + OPERADOR + ")");

	public static String [] separar( String expresion) {
		Matcher comparador = PATRON.matcher(expresion);
		
		List<String> separado = new ArrayList<String>();

		while (comparador.find()) {

			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
								: comparador.group("REAL") != null ? "T-REAL"
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

/** MEJORA CON VECTORES **/
//		int tamaño = String.join(" ", expresionVec).replace("(", "").replace(")", "").split(" ").length ;
		
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
				subexpresion="";
				
			} else if (esDato(expresionVec[i])) {
				resultado = resultado + " " + expresionVec[i];
				i++;
			} else if (pila.empty()) {
				pila.push(expresionVec[i]);
				i++;
			} else {
				//System.out.println(expresionVec[i]+"-"+jerarquia(expresionVec[i])+ " "+pila.peek()+"-"+ jerarquia(pila.peek()));
				if (jerarquia(expresionVec[i]) < jerarquia(pila.peek())) {
					resultado = resultado + " " + pila.peek();
					pila.pop();
				} else if (jerarquia(expresionVec[i]) == jerarquia(pila.peek())) {
					while (!pila.empty() ) {
						if(jerarquia(expresionVec[i]) <= jerarquia(pila.peek())) {
							resultado = resultado + " " + pila.peek();
							pila.pop();
						}else {
							break;
						}
						
					}
					pila.push(expresionVec[i]);
					i++;
				} else if (jerarquia(expresionVec[i]) > jerarquia(pila.peek())) {
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
		//resultado = resultado.substring(1, resultado.length());
		return resultado;
	}
	
	
	public static boolean esDato(String identificador) {
		Matcher comparador = PATRON.matcher(identificador);
		if (comparador.find()) {
			String tipoDeDato = comparador.group("ENTERO") != null ? "T-ENTERO"
					: comparador.group("REAL") != null ? "T-REAL"
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
	
	public static int jerarquia(String operador) {

		int rta = (operador.matches("\\|\\|")) ? 1
				: (operador.matches("\\&\\&")) ? 2
				: (operador.matches("\\=\\=|\\!\\=|\\>|\\<|\\<\\=|\\>\\=")) ? 3
				: (operador.matches("\\+|\\-")) ? 4
				: (operador.matches("\\%")) ? 6
				: (operador.matches("\\*|\\/")) ? 6
				: (operador.matches("\\^")) ? 7
				: (operador.matches("\\(|\\)")) ? 8 : 0;
		return rta;
	}
}
