package logica;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface Evaluador {
	
	 static final String ENTERO = "[0-9]+";
	 static final String DECIMAL = "[0-9]+\\.[0-9]+";
	 static final String LOGICO = "true|false";
	 static final String TEXTO = "\"([^\"\\\\]|\\\\.)*\"";
	 static final String PARENTESIS = "\\(|\\)";
	static final String OPERADOR = "\\^|\\/|\\*|\\+|\\-|\\%|\\=\\=|\\=!|\\>\\=|\\<\\=|\\<|\\>|\\&\\&|\\|\\|";
	
	
	// private static final String COMENTARIO = "//[^\n]*" + "|" +
	// "/\\*(.|\\R)*?\\*/";

	static final String IDENTIFICADOR = "[a-z]+([0-9]+)?";

	 static Pattern PATRON = Pattern.compile("(?<LOGICO>" + LOGICO + ")" + "|(?<DECIMAL>" + DECIMAL + ")"
			+ "|(?<ENTERO>" + ENTERO + ")" + "|(?<IDENTIFICADOR>" + IDENTIFICADOR + ")" + "|(?<TEXTO>" + TEXTO + ")"
			+ "|(?<PARENTESIS>" + PARENTESIS + ")" + "|(?<OPERADOR>" + OPERADOR + ")");


	public static Variable evaluar(String expresionPostFija, TablaDeSimbolos ts) {
		Matcher comparador = PATRON.matcher(expresionPostFija);
		Stack<Variable> lista = new Stack<Variable>();
		String tipoDeDato;
		
		while (comparador.find()) {
			tipoDeDato = comparador.group("DECIMAL") != null ? "D"
						: comparador.group("ENTERO") != null ? "E"
						: comparador.group("LOGICO") != null ? "L"
						: comparador.group("TEXTO") != null ?  "T"
						: comparador.group("IDENTIFICADOR") != null ? "I"
						: comparador.group("OPERADOR") != null ? "O" : "R";

			if (!tipoDeDato.equals("O")) {
				if (tipoDeDato.equals("I")) {
					lista.push(ts.get(ts.hash(comparador.group())));
				} else if (tipoDeDato.equals("T")) {
					String textoSinComillas = comparador.group().replaceAll("\"", "");
					lista.push(new Variable(textoSinComillas, tipoDeDato, null));
				} else {
					lista.push(new Variable(comparador.group(), tipoDeDato, null));
				}
			} else if (lista.size() > 1) {
				Variable operando2 = lista.peek();
				lista.pop();
				Variable operando1 = lista.peek();
				lista.pop();
				//System.out.println("A operar: "+operando1.getValor() + " " + comparador.group() + " " + operando2.getValor());
				String resultado[] = operar(operando1.getValor(), operando1.getTipo(), comparador.group(), operando2.getValor(), operando2.getTipo());
				if (resultado[0].equals("R")) {
					return (new Variable("R", "R", null));
				}
				String valor = resultado[0];
				String tipo = resultado[1];
				//System.out.println("= " + valor + " " + tipo);
				lista.push(new Variable(valor, tipo, null));
			} else {
				Variable a = lista.peek();
				a.setValor(comparador.group() + a.getValor());
			}
		}
		return  (new Variable(lista.peek().getValor(), lista.peek().getTipo(), null));
	}
	
	public static String[] operar(String operando1, String tipo1, String operador, String operando2, String tipo2) {
		
		int operando1Entero = 0;
		double operando1Decimal = 0;
		boolean operando1Logico = false;
		String operando1Texto = "";

		int operando2Entero = 0;
		double operando2Decimal = 0;
		boolean operando2Logico = false;
		String operando2Texto = "";

		if (tipo1.equals("E")) {
			operando1Entero = Integer.parseInt(operando1);
		} else if (tipo1.equals("D")) {
			operando1Decimal = Double.parseDouble(operando1);
		} else if (tipo1.equals("L")) {
			operando1Logico = Boolean.parseBoolean(operando1);
		} else if (tipo1.equals("T")) {
			operando1Texto = operando1;
		}

		if (tipo2.equals("E")) {
			operando2Entero = Integer.parseInt(operando2);
		} else if (tipo2.equals("D")) {
			operando2Decimal = Double.parseDouble(operando2);
		} else if (tipo2.equals("L")) {
			operando2Logico = Boolean.parseBoolean(operando2);
		} else if (tipo2.equals("T") ) {
			operando2Texto = operando2;
		}

		/************************ OPERANDO *************************************/
		String vec[] = new String[2];
		if (tipo1.equals("D") && tipo2.equals("E")) {
			if (operador.equals("^")) {
				vec[0] = Math.pow(operando1Decimal, operando2Entero) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("/")) {
				vec[0] = (operando1Decimal / operando2Entero) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("*")) {
				vec[0] = (operando1Decimal * operando2Entero) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("+")) {
				vec[0] = (operando1Decimal + operando2Entero) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("-")) {
				vec[0] = (operando1Decimal - operando2Entero) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("%")) {
				vec[0] = (operando1Decimal % operando2Entero) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec[0] = (operando1Decimal == operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec[0] = (operando1Decimal != operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec[0] = (operando1Decimal > operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec[0] = (operando1Decimal < operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec[0] = (operando1Decimal <= operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec[0] = (operando1Decimal >= operando2Entero) + "";
				vec[1] = "L";
				return vec;
			}
			/*************************************************************/
		} else if (tipo1.equals("E") && tipo2.equals("D") ) {
			if (operador.equals("^")) {
				vec[0] = Math.pow(operando1Entero, operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("/")) {
				vec[0] = (operando1Entero / operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("*")) {
				vec[0] = (operando1Entero * operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("+")) {
				vec[0] = (operando1Entero + operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("-")) {
				vec[0] = (operando1Entero - operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("%")) {
				vec[0] = (operando1Entero % operando2Decimal) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec[0] = (operando1Entero == operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec[0] = (operando1Entero != operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec[0] = (operando1Entero > operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec[0] = (operando1Entero < operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec[0] = (operando1Entero <= operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec[0] = (operando1Entero >= operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("D") && tipo2.equals("T") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Decimal + operando2Texto) + "";
				vec[1] = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("D") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Texto + operando2Decimal) + "";
				vec[1] = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("E") && tipo2.equals("T") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Entero + operando2Texto) + "";
				vec[1] = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("E") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Texto + operando2Entero) + "";
				vec[1] = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("L") && tipo2.equals("T") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Logico + operando2Texto) + "";
				vec[1] = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("L") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Texto + operando2Logico) + "";
				vec[1] = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("D") && tipo2.equals("D") ) {
			if (operador.equals("^")) {
				vec[0] = Math.pow(operando1Decimal, operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("/")) {
				vec[0] = (operando1Decimal / operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("*")) {
				vec[0] = (operando1Decimal * operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("+")) {
				vec[0] = (operando1Decimal + operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("-")) {
				vec[0] = (operando1Decimal - operando2Decimal) + "";
				vec[1] = "D";
				return vec;
			} else if (operador.equals("%")) {
				// VERIFICAR
				vec[0] = (operando1Decimal % operando2Decimal) + "";
				vec[1] = "E";
				return vec;
				// VERIFICAR
			} else if (operador.equals("==")) {
				vec[0] = (operando1Decimal == operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec[0] = (operando1Decimal != operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec[0] = (operando1Decimal > operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec[0] = (operando1Decimal < operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec[0] = (operando1Decimal <= operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec[0] = (operando1Decimal >= operando2Decimal) + "";
				vec[1] = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("T") ) {
			if (operador.equals("+")) {
				vec[0] = (operando1Texto + operando2Texto) + "";
				vec[1] = "T";
				return vec;
			} else if (operador.equals("==")) {
				vec[0] = (operando1Texto == operando2Texto) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec[0] = (operando1Texto != operando2Texto) + "";
				vec[1] = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("E") && tipo2.equals("E") ) {
			if (operador.equals("^")) {
				vec[0] = (int) (Math.pow(operando1Entero, operando2Entero)) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("/")) {
				vec[0] = (operando1Entero / operando2Entero) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("*")) {
				vec[0] = (operando1Entero * operando2Entero) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("+")) {
				vec[0] = (operando1Entero + operando2Entero) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("-")) {
				vec[0] = (operando1Entero - operando2Entero) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("%")) {
				vec[0] = (operando1Entero % operando2Entero) + "";
				vec[1] = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec[0] = (operando1Entero == operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec[0] = (operando1Entero != operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">")) {
				vec[0] = (operando1Entero > operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<")) {
				vec[0] = (operando1Entero < operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("<=")) {
				vec[0] = (operando1Entero <= operando2Entero) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals(">=")) {
				vec[0] = (operando1Entero >= operando2Entero) + "";
				vec[1] = "L";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("L") && tipo2.equals("L") ) {
			if (operador.equals("==")) {
				vec[0] = (operando1Logico == operando2Logico) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("=!")) {
				vec[0] = (operando1Logico != operando2Logico) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("&&")) {
				vec[0] = (operando1Logico && operando2Logico) + "";
				vec[1] = "L";
				return vec;
			} else if (operador.equals("||")) {
				vec[0] = (operando1Logico || operando2Logico) + "";
				vec[1] = "L";
				return vec;
			}

		}
		vec[0] = "R";
		vec[1] = "R";

		return vec;
	}
}
