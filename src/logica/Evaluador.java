package logica;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Evaluador {

	static final String ENTERO = "[0-9]+";
	static final String REAL = "[0-9]+\\.[0-9]+";
	static final String LOGICO = "verdadero|falso";
	static final String TEXTO = "\"([^\"\\\\]|\\\\.)*\"";
	static final String PARENTESIS = "\\(|\\)";
	static final String OPERADOR = "\\^|\\/|\\*|\\+|\\-|\\%|\\=\\=|\\!\\=|\\>\\=|\\<\\=|\\<|\\>|\\&\\&|\\|\\|";

	// private static final String COMENTARIO = "//[^\n]*" + "|" +
	// "/\\*(.|\\R)*?\\*/";

	static final String IDENTIFICADOR = "[a-z]+([0-9]+)?";

	static Pattern PATRON = Pattern.compile("(?<LOGICO>" + LOGICO + ")" + "|(?<REAL>" + REAL + ")" + "|(?<ENTERO>"
			+ ENTERO + ")" + "|(?<IDENTIFICADOR>" + IDENTIFICADOR + ")" + "|(?<TEXTO>" + TEXTO + ")" + "|(?<PARENTESIS>"
			+ PARENTESIS + ")" + "|(?<OPERADOR>" + OPERADOR + ")");

	public static Variable evaluar(String expresionPostFija, TablaDeSimbolos ts) {
		
//		System.out.println(expresionPostFija);
		
		Matcher comparador = PATRON.matcher(expresionPostFija);
		Stack<Variable> lista = new Stack<Variable>();
		String tipoDeDato;

		while (comparador.find()) {
			tipoDeDato = comparador.group("REAL") != null ? "R"
					: comparador.group("ENTERO") != null ? "E"
					: comparador.group("LOGICO") != null ? "L"
					: comparador.group("TEXTO") != null ? "T"
					: comparador.group("IDENTIFICADOR") != null ? "I"
					: comparador.group("OPERADOR") != null ? "O" : "X";
			/** recuperacion rapida de error con condicional en X */
			if (!tipoDeDato.equals("O")) {
				if (tipoDeDato.equals("I")) {
					lista.push(ts.get(ts.hash(comparador.group())));
				} else if (tipoDeDato.equals("T")) {
					String textoSinComillas = comparador.group().replaceAll("\"", "");
					lista.push(new Variable(textoSinComillas, tipoDeDato, ""));
				} else {
					lista.push(new Variable(comparador.group(), tipoDeDato, ""));
				}
			} else if (lista.size() > 1) {
				Variable operando2 = lista.peek();
				lista.pop();
				Variable operando1 = lista.peek();
				lista.pop();
//	DEBUG
//				System.out.println("A operar: ["+operando1.getValor() + "] " + comparador.group() + " [" + operando2.getValor()+"]");
				String resultado[] = operar(operando1.getValor(), operando1.getTipo(), comparador.group(),
						operando2.getValor(), operando2.getTipo());

				if (resultado[0].equals("X")) {
					return (new Variable("X", "X", ""));
				}
				String valor = resultado[0];
				String tipo = resultado[1];
//	DEBUG
//				System.out.println("= " + valor + " " + tipo);
				lista.push(new Variable(valor, tipo, ""));
			} else {
				Variable a = lista.peek();
				a.setValor(comparador.group() + a.getValor());
			}
		}
		return (new Variable(lista.peek().getValor(), lista.peek().getTipo(), ""));
	}

	public static String evaluarSemanticamente(String expresionPostFija, TablaDeSimbolos ts) {
		Matcher comparador = PATRON.matcher(expresionPostFija);
		Stack<String> lista = new Stack<String>();
		String tipoDeDato;
		String vec = "X";

		while (comparador.find()) {

			tipoDeDato = comparador.group("REAL") != null ? "R"
					: comparador.group("ENTERO") != null ? "E"
							: comparador.group("LOGICO") != null ? "L"
									: comparador.group("TEXTO") != null ? "T"
											: comparador.group("IDENTIFICADOR") != null ? "I"
													: comparador.group("OPERADOR") != null ? "O"
															: comparador.group("ESPACIO") != null ? "S" : "X";

			if (!tipoDeDato.equals("S")) {
				if (!tipoDeDato.equals("O")) {
					if (tipoDeDato.equals("I")) {
						lista.push(ts.get(ts.hash(comparador.group())).getTipo());
					} else if (tipoDeDato.equals("T")) {
						// String textoSinComillas = comparador.group().replaceAll("\"","");
						lista.push(tipoDeDato);
					} else {
						lista.push(tipoDeDato);
					}
				} else if (lista.size() > 1) {
					String operando2Tipo = lista.peek();
					lista.pop();
					String operando1Tipo = lista.peek();
					lista.pop();

					// errores.add(operando1.getValor() + " " + comparador.group() + " " +
					// operando2.getValor());

					String resultado = operar(operando1Tipo, comparador.group(), operando2Tipo);

					if (resultado.equals("X")) {
						return vec;
					}
					lista.push(resultado);
				}
				// ---- NEGATIVO
				// else {
				// Variable a = lista.peek();
				// a.setValor(comparador.group() + a.getValor());

				// }
			}
		}
		vec = lista.peek();
		return vec;
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
		} else if (tipo1.equals("R")) {
			operando1Decimal = Double.parseDouble(operando1);
		} else if (tipo1.equals("L")) {
			if (operando1.equals("verdadero")) {
				operando1Logico = true;
			} else {
				operando1Logico = false;
			}
		} else if (tipo1.equals("T")) {
			operando1Texto = operando1;
		}

		if (tipo2.equals("E")) {
			operando2Entero = Integer.parseInt(operando2);
		} else if (tipo2.equals("R")) {
			operando2Decimal = Double.parseDouble(operando2);
		} else if (tipo2.equals("L")) {
			if (operando2.equals("verdadero")) {
				operando2Logico = true;
			} else {
				operando2Logico = false;
			}
		} else if (tipo2.equals("T")) {
			operando2Texto = operando2;
		}

		/************************ OPERANDO *************************************/
		String vec[] = new String[2];
		try {

			if (tipo1.equals("R") && tipo2.equals("E")) {
				if (operador.equals("^")) {
					vec[0] = Math.pow(operando1Decimal, operando2Entero) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("/")) {
					vec[0] = (operando1Decimal / operando2Entero) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("*")) {
					vec[0] = (operando1Decimal * operando2Entero) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("+")) {
					vec[0] = (operando1Decimal + operando2Entero) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("-")) {
					vec[0] = (operando1Decimal - operando2Entero) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("%")) {
					vec[0] = (operando1Decimal % operando2Entero) + "";
					vec[1] = "E";
					return vec;
				} else if (operador.equals("==")) {
					if (operando1Decimal == operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("!=")) {
					/** REVISARRRRR != POR != **/
					if (operando1Decimal != operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">")) {
					if (operando1Decimal > operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<")) {
					if (operando1Decimal < operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<=")) {
					if (operando1Decimal <= operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">=")) {
					if (operando1Decimal >= operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				}
				/*************************************************************/
			} else if (tipo1.equals("E") && tipo2.equals("R")) {
				if (operador.equals("^")) {
					vec[0] = Math.pow(operando1Entero, operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("/")) {
					vec[0] = (operando1Entero / operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("*")) {
					vec[0] = (operando1Entero * operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("+")) {
					vec[0] = (operando1Entero + operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("-")) {
					vec[0] = (operando1Entero - operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("%")) {
					vec[0] = (operando1Entero % operando2Decimal) + "";
					vec[1] = "E";
					return vec;
				} else if (operador.equals("==")) {
					if (operando1Entero == operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("!=")) {
					/** Revisar != por != **/
					if (operando1Entero != operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">")) {
					if (operando1Entero > operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<")) {
					if (operando1Entero < operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<=")) {
					if (operando1Entero <= operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">=")) {
					if (operando1Entero >= operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("R") && tipo2.equals("T")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Decimal + operando2Texto) + "";
					vec[1] = "T";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("T") && tipo2.equals("R")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Texto + operando2Decimal) + "";
					vec[1] = "T";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("E") && tipo2.equals("T")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Entero + operando2Texto) + "";
					vec[1] = "T";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("T") && tipo2.equals("E")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Texto + operando2Entero) + "";
					vec[1] = "T";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("L") && tipo2.equals("T")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Logico + operando2Texto) + "";
					vec[1] = "T";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("T") && tipo2.equals("L")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Texto + operando2Logico) + "";
					vec[1] = "T";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("R") && tipo2.equals("R")) {
				if (operador.equals("^")) {
					vec[0] = Math.pow(operando1Decimal, operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("/")) {
					vec[0] = (operando1Decimal / operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("*")) {
					vec[0] = (operando1Decimal * operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("+")) {
					vec[0] = (operando1Decimal + operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("-")) {
					vec[0] = (operando1Decimal - operando2Decimal) + "";
					vec[1] = "R";
					return vec;
				} else if (operador.equals("%")) {
					// VERIFICAR
					vec[0] = (operando1Decimal % operando2Decimal) + "";
					vec[1] = "E";
					return vec;
					// VERIFICAR
				} else if (operador.equals("==")) {
					if (operando1Decimal == operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("!=")) {
					/** REVISAR != POR != **/
					if (operando1Decimal != operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">")) {
					if (operando1Decimal > operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<")) {
					if (operando1Decimal < operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<=")) {
					if (operando1Decimal <= operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">=")) {
					if (operando1Decimal >= operando2Decimal) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("T") && tipo2.equals("T")) {
				if (operador.equals("+")) {
					vec[0] = (operando1Texto + operando2Texto) + "";
					vec[1] = "T";
					return vec;
				} else if (operador.equals("==")) {
					if (operando1Texto.equals(operando2Texto)) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("!=")) {
					if (!operando1Texto.equals(operando2Texto)) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("E") && tipo2.equals("E")) {
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
					if (operando1Entero == operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("!=")) {
					/** REVISAR != POR != **/
					if (operando1Entero != operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">")) {
					if (operando1Entero > operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<")) {
					if (operando1Entero < operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("<=")) {
					if (operando1Entero <= operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals(">=")) {
					if (operando1Entero >= operando2Entero) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				}
				/*-***********************************************************-*/
			} else if (tipo1.equals("L") && tipo2.equals("L")) {
				if (operador.equals("==")) {
					if (operando1Logico == operando2Logico) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("!=")) {
					if (operando1Logico != operando2Logico) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("&&")) {
					if (operando1Logico && operando2Logico) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				} else if (operador.equals("||")) {
					if (operando1Logico || operando2Logico) {
						vec[0] = "verdadero";
					} else {
						vec[0] = "falso";
					}
					vec[1] = "L";
					return vec;
				}

			}
		} catch (Exception e) {
			
		}
		vec[0] = "X";
		vec[1] = "X";

		return vec;
	}

	public static String operar(String tipo1, String operador, String tipo2) {

		/************************ OPERANDO *************************************/
		String vec = "X";
		if (tipo1.equals("R") && tipo2.equals("E")) {
			if (operador.equals("^")) {
				vec = "R";
				return vec;
			} else if (operador.equals("/")) {
				vec = "R";
				return vec;
			} else if (operador.equals("*")) {
				vec = "R";
				return vec;
			} else if (operador.equals("+")) {
				vec = "R";
				return vec;
			} else if (operador.equals("-")) {
				vec = "R";
				return vec;
			} else if (operador.equals("%")) {
				vec = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("!=")) {
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
		} else if (tipo1.equals("E") && tipo2.equals("R")) {
			if (operador.equals("^")) {
				vec = "R";
				return vec;
			} else if (operador.equals("/")) {
				vec = "R";
				return vec;
			} else if (operador.equals("*")) {
				vec = "R";
				return vec;
			} else if (operador.equals("+")) {
				vec = "R";
				return vec;
			} else if (operador.equals("-")) {
				vec = "R";
				return vec;
			} else if (operador.equals("%")) {
				vec = "E";
				return vec;
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("!=")) {
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
		} else if (tipo1.equals("R") && tipo2.equals("T")) {
			if (operador.equals("+")) {
				vec = "T";
				return vec;
			}
			/*-***********************************************************-*/
		} else if (tipo1.equals("T") && tipo2.equals("R")) {
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
		} else if (tipo1.equals("R") && tipo2.equals("R")) {
			if (operador.equals("^")) {
				vec = "R";
				return vec;
			} else if (operador.equals("/")) {
				vec = "R";
				return vec;
			} else if (operador.equals("*")) {
				vec = "R";
				return vec;
			} else if (operador.equals("+")) {
				vec = "R";
				return vec;
			} else if (operador.equals("-")) {
				vec = "R";
				return vec;
			} else if (operador.equals("%")) {
				// VERIFICAR
				vec = "E";
				return vec;
				// VERIFICAR
			} else if (operador.equals("==")) {
				vec = "L";
				return vec;
			} else if (operador.equals("!=")) {
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
			} else if (operador.equals("!=")) {
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
			} else if (operador.equals("!=")) {
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
			} else if (operador.equals("!=")) {
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

		vec = "X";

		return vec;
	}

	public static String convertir(String valor, String tipo) {
		if (tipo.equals("E")) {
			try {
				String rta = Integer.parseInt(valor) + "";
				return rta;
			} catch (Exception e) {
				return "";
			}
		} else if (tipo.equals("R")) {
			try {
				String rta = Double.parseDouble(valor) + "";
				return rta;
			} catch (Exception e) {
				return "";
			}
		} else if (tipo.equals("L")) {
			try {
				String rta = Boolean.parseBoolean(valor) + "";
				return rta;
			} catch (Exception e) {
				return "";
			}
		}
		return valor;
	}
}
