package logica;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controlador.Controlador;
import javafx.application.Platform;
import javafx.geometry.Pos;

public class Ejecutor implements Evaluador, Posfijo {
	Registro rt;
	TablaDeSimbolos ts;
	Controlador ctrl;
	ArrayList<String> info;
	ArrayList<NodoSecuenciador> instrucciones;
	int indiceDeInstruccion, numeroDeLinea, lineaDeEscritura;
	boolean esperando, corriendo;
	String identificadorLectura;
	Thread hilo;

	public Ejecutor(Controlador ctrl) {
		rt = new Registro();
		this.ctrl = ctrl;
		info = new ArrayList<String>();
		esperando = false;
	}

	// UTILIZADO PARA EJECUTAR EN HILO
//	public void ejecutar(ArrayList<NodoSecuenciador> instrucciones) {
//		int indice = 0;
//		NodoSecuenciador actual = instrucciones.get(indice);
//		info = new ArrayList<String>();
//		info.add("================ INFORMACION ================");
//
//		while (!actual.instruccion.contains("end")) {
//			if (actual.instruccion.equals("jump")) {
//				indice = actual.salto - 1;
//			} else {
//				if (actual.tipo.contains("Declaracion")) {
//					if (actual.instruccion.contains("=")) {
//						String declaracion[] = actual.instruccion.split("=");
//						String primeraParte[] = declaracion[0].trim().replaceAll(" +", " ").split(" ");
//						declaracion[1] = declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
//						declaracion(Character.toUpperCase(primeraParte[0].charAt(0)) + "", primeraParte[1],
//								declaracion[1]);
//						indice++;
//					} else {
//						info.add(actual.instruccion);
//					}
//				} else if (actual.tipo.equals("Asignacion")) {
//					String declaracion[] = actual.instruccion.split("=");
//					declaracion[0] = declaracion[0].trim().replaceAll(" +", " ");
//					declaracion[1] = declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
//					asignacion(declaracion[0], declaracion[1]);
//					indice++;
//				} else if (actual.tipo.contains("condicional")) {
//					indice = evaluarCondicion(actual.instruccion) ? indice + 1 : actual.salto - 1;
//				} else if (actual.tipo.contains("Mientras")) {
//					indice = evaluarCondicion(actual.instruccion) ? indice + 1 : actual.salto - 1;
//				} else if (actual.tipo.contains("repetir")) {
//					if (Integer.parseInt(actual.instruccion) == 0) {
//						actual.instruccion = ts.get(ts.hash(actual.tipo)).valor;
//						indice = actual.salto - 1;
//					} else {
//						actual.instruccion = (Integer.parseInt(actual.instruccion) - 1) + "";
//						indice++;
//					}
//					ctrl.actualizarRepetir(actual.instruccion, actual.numeroDeLinea);
//				}
//			}
//
//			actual = instrucciones.get(indice);
//		}
//		ctrl.presentarErrores(info, "ej");
//	}

	/** REVISAR DESDE PARTES **/

//	public void ejecutarSiguienteInstruccion() {
//		NodoSecuenciador actual;
//		if (indiceDeInstruccion < instrucciones.size()) {
//			actual = instrucciones.get(indiceDeInstruccion);
//		} else {
//			actual = instrucciones.get(instrucciones.size() - 1);
//		}
//		numeroDeLinea = actual.getNumeroDeLinea();
//		ctrl.señalarLineaEnCodigo(numeroDeLinea);
//		info = new ArrayList<String>();
//		info.add("================ INFORMACION ================");
//		
//		if (esperando) {
//			leer();
//		} else {
//			if (!actual.instruccion.contains("end")) {
//				if (actual.instruccion.equals("jump")) {
//					indiceDeInstruccion = actual.salto - 1;
//				} else {
//					if (actual.tipo.contains("Declaracion")) {
//						if (actual.instruccion.contains("=")) {
//							String declaracion[] = actual.instruccion.split("=");
//							String primeraParte[] = declaracion[0].trim().replaceAll(" +", " ").split(" ");
//							declaracion[1] = declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
//							declaracion(Character.toUpperCase(primeraParte[0].charAt(0)) + "", primeraParte[1],
//									declaracion[1]);
//							indiceDeInstruccion++;
//						} else {
//							Pattern pattern = Pattern.compile("entero|real|logico|texto");
//							Matcher matcher = pattern.matcher(actual.instruccion);
//							if (matcher.find()) {
//								int fin = matcher.end();
//								String segundaParte = actual.instruccion.substring(fin).replace(";", "").replace(" ",
//										"");
//								info.add(segundaParte);
//								String tipo = Character.toUpperCase(actual.instruccion.substring(0, fin).charAt(0))
//										+ "";
//								String variables[] = segundaParte.split(",");
//								for (int i = 0; i < variables.length; i++) {
//									declaracion(tipo, variables[i]);
//								}
//								indiceDeInstruccion++;
//							}
//
//						}
//					} else if (actual.tipo.equals("Asignacion")) {
//						if (actual.instruccion.contains("Escribir")) {
//							Pattern pattern = Pattern.compile("Escribir\\(|\\)\\;");
//							Matcher matcher = pattern.matcher(actual.instruccion);
//							matcher.find();
//							int inicioExpresion = matcher.end();
//							matcher.find();
//							int finExpresion = matcher.start();
//							info.add(actual.instruccion.substring(inicioExpresion, finExpresion));
//							Variable v = Evaluador.evaluar(Posfijo.postfija(
//									Posfijo.separar(actual.instruccion.substring(inicioExpresion, finExpresion))), ts);
//							info.add(v.valor);
//							ctrl.escribirEnConsola(v.valor, numeroDeLinea);
//							indiceDeInstruccion++;
//						} else if(actual.instruccion.contains("Leer")){
//							esperando=true;
//							identificadorLectura =  actual.instruccion.split("=")[0].trim();
//						}else {
//							String declaracion[] = actual.instruccion.split("=");
//							declaracion[0] = declaracion[0].trim().replaceAll(" +", " ");
//							declaracion[1] = declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
//							asignacion(declaracion[0], declaracion[1]);
//							indiceDeInstruccion++;
//						}
//						
//					} else if (actual.tipo.contains("condicional")) {
//						indiceDeInstruccion = evaluarCondicion(actual.instruccion) ? indiceDeInstruccion + 1
//								: actual.salto - 1;
//					} else if (actual.tipo.contains("Mientras")) {
//						indiceDeInstruccion = evaluarCondicion(actual.instruccion) ? indiceDeInstruccion + 1
//								: actual.salto - 1;
//					} else if (actual.tipo.contains("repetir")) {
//						if (Integer.parseInt(actual.instruccion) == 0) {
//							actual.instruccion = ts.get(ts.hash(actual.tipo)).valor;
//							indiceDeInstruccion = actual.salto - 1;
//						} else {
//							actual.instruccion = (Integer.parseInt(actual.instruccion) - 1) + "";
//							indiceDeInstruccion++;
//						}
//						ctrl.actualizarRepetir(actual.instruccion, actual.numeroDeLinea);
//					}
//				}
//			}
//			ctrl.presentarErrores(info, "ej");
//		}
//	}

	public void ejecutarAutomaticamente() {

		hilo = new Thread() {
			public void run() {
				NodoSecuenciador actual = instrucciones.get(indiceDeInstruccion);
				corriendo = true;
				while (!actual.getExpresiones().getPrimeraParte().contains("end")) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					Platform.runLater(new Runnable() {
						public void run() {
							ejecutarSiguienteInstruccion();
						}
					});
					if (indiceDeInstruccion < instrucciones.size()) {
						actual = instrucciones.get(indiceDeInstruccion);
					} else {
						actual = instrucciones.get(instrucciones.size() - 1);
					}
				}
				corriendo = false;
				hilo.interrupt();
				
				return;
			}
		};
		
		hilo.start();
		
	}

	/********************** +++ ******************************/

	public void ejecutarSiguienteInstruccion() {
		NodoSecuenciador actual;
		if (indiceDeInstruccion < instrucciones.size()) {
			actual = instrucciones.get(indiceDeInstruccion);
		} else {
			actual = instrucciones.get(instrucciones.size() - 1);
		}
		numeroDeLinea = actual.getNumeroDeLinea();
		ctrl.señalarLineaEnCodigo(numeroDeLinea);
		info = new ArrayList<String>();
		info.add("================ INFORMACION ================");

		if (esperando) {
			leer();
		} else {
			if (!actual.getExpresiones().getPrimeraParte().contains("end")) {
				if (actual.getExpresiones().getPrimeraParte().equals("jump")) {
					indiceDeInstruccion = actual.getSalto() - 1;
				} else {
					if (actual.getTipo().contains("Declaracion")) {
						if (actual.getExpresiones().getSegundaParte() != null) {
//		DEBUG		-------------------------------------					
							info.add("Declaracion " + actual.getExpresiones().getTipo() + " "
									+ actual.getExpresiones().getPrimeraParte() + " "
									+ actual.getExpresiones().getSegundaParte());
//		DEBUG		-------------------------------------
							declaracion(actual.getExpresiones().getTipo(), actual.getExpresiones().getPrimeraParte(),
									actual.getExpresiones().getSegundaParte());
							indiceDeInstruccion++;
						} else {
							String tipo = actual.getExpresiones().getTipo();
							String variables[] = actual.getExpresiones().getPrimeraParte().split(",");
//		DEBUG		-------------------------------------
							info.add("Declaracion " + tipo + " " + variables.toString());
//		DEBUG		-------------------------------------
							for (int i = 0; i < variables.length; i++) {
								declaracion(tipo, variables[i].trim());
							}
							indiceDeInstruccion++;
						}
					} else if (actual.getTipo().contains("Asignacion")) {
//		DEBUG		-------------------------------------
						info.add("Asignacion " + actual.getExpresiones().getPrimeraParte() + " = "
								+ actual.getExpresiones().getSegundaParte());
//		DEBUG		-------------------------------------
						asignacion(actual.getExpresiones().getPrimeraParte(),
								actual.getExpresiones().getSegundaParte());
						indiceDeInstruccion++;

					} else if (actual.getTipo().contains("Lectura")) {
						esperando = true;
						identificadorLectura = actual.getExpresiones().getPrimeraParte().trim();
//		DEBUG		-------------------------------------
						info.add("Lectura " + identificadorLectura);
//		DEBUG		-------------------------------------
					} else if (actual.tipo.contains("Escritura")) {
						Variable v = Evaluador.evaluar(actual.getExpresiones().getPrimeraParte(), ts);
						if (v.getValor().equals("X")) {
							info.add(
									"Ocurrio un error al intentar operar " + actual.getExpresiones().getPrimeraParte());
						} else {
//		DEBUG		-------------------------------------				
							info.add("Escritura " + actual.getExpresiones().getPrimeraParte() + " valor:" + v.valor);
//		DEBUG		-------------------------------------
							ctrl.escribirEnConsola(v.valor, numeroDeLinea);
							indiceDeInstruccion++;
						}

					} else if (actual.getTipo().contains("condicional")) {
//		DEBUG		-------------------------------------
						info.add("Condicional " + actual.getExpresiones().getPrimeraParte());
						info.add("Condicional " + evaluarCondicion(actual.getExpresiones().getPrimeraParte()));
						info.add("Condicional " +  (indiceDeInstruccion + 1));
						info.add("Condicional " +  actual.salto);
//		DEBUG		-------------------------------------
						indiceDeInstruccion = evaluarCondicion(actual.getExpresiones().getPrimeraParte())
								? indiceDeInstruccion + 1
								: actual.salto -1;
					} else if (actual.getTipo().contains("Mientras")) {
//		DEBUG		-------------------------------------	
						info.add("Mientras " + actual.getExpresiones().getPrimeraParte());
//		DEBUG		-------------------------------------		
						indiceDeInstruccion = evaluarCondicion(actual.getExpresiones().getPrimeraParte())
								? indiceDeInstruccion + 1
								: actual.salto - 1;
					} else if (actual.getTipo().contains("repetir")) {
//		DEBUG		-------------------------------------
						info.add("Repetir " + actual.getExpresiones().getPrimeraParte());
//		DEBUG		-------------------------------------
						
						if (Integer.parseInt(actual.getExpresiones().getPrimeraParte().trim()) == 0) {
							
							actual.getExpresiones().setPrimeraParte(ts.get(ts.hash(actual.tipo)).valor);
							indiceDeInstruccion = actual.salto - 1;
						} else {
							actual.getExpresiones().setPrimeraParte(
									Integer.parseInt(actual.getExpresiones().getPrimeraParte().trim()) - 1 + "");
							indiceDeInstruccion++;
						}
						ctrl.actualizarRepetir(actual.getExpresiones().getPrimeraParte(), actual.numeroDeLinea);
					}
				}
			}
			ctrl.presentarErrores(info, "ej");
		}
	}

	/**
	 * IMPRESIONES PARA DEBUG: String[] separado = Posfijo.separar(expresion);
	 * String posfija= Posfijo.postfija(separado);
	 * System.out.println("---------------------------------");
	 * System.out.println(Arrays.toString(separado)); System.out.println("Ejecutor:
	 * "+posfija);
	 **/

	/** LIMPIAR SEPARADO Y POSFIJAS **/

	public void declaracion(String tipo, String identificador, String expresion) {
//		Variable resultado = Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)), ts);
//		------------------------------------------------------
		identificador = identificador.trim();
		Variable resultado = Evaluador.evaluar(expresion, ts);
		if (resultado.getValor().equals("X")) {
			info.add("Ocurrio un error al intentar operar " + expresion);
		} else {
			info.add(numeroDeLinea + "+ Declaracion: En identificador " + identificador + " ~ " + expresion + " = "
					+ resultado.valor);
//			------------------------------------------------------
			resultado.nombre = identificador;
			if (tipo.equals(resultado.tipo)) {
				ts.put(ts.hash(identificador), resultado);
				info.add("+ L:" + (numeroDeLinea) + "~ Se declara la variable " + identificador + " : "
						+ resultado.valor);
			} else if (tipo.equals("R") && resultado.tipo.equals("E")) {
				ts.put(ts.hash(identificador), resultado);
				info.add("+ L:" + (numeroDeLinea) + "~ Se declara la variable " + identificador + " : "
						+ resultado.valor);
			}
			ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
		}

	}

	public void declaracion(String tipo, String identificador) {
		identificador = identificador.trim();
		Variable resultado = new Variable("", tipo, identificador);
		info.add(numeroDeLinea + "+ Declaracion: En identificador " + identificador + " = " + resultado.valor);
		ts.put(ts.hash(identificador), resultado);
		info.add("+ L:" + (numeroDeLinea) + "~ Se declara la variable " + identificador + " : " + resultado.valor);
		ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
	}

	public void asignacion(String identificador, String expresion) {
		/** IMPRESION DEBUG **/
//		System.out.println(Posfijo.separar(expresion));
//		System.out.println(Posfijo.postfija(Posfijo.separar(expresion)));

//		Variable resultado = Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)), ts);

//		------------------------------------------------------
		identificador = identificador.trim();
		Variable resultado = Evaluador.evaluar(expresion, ts);
		if (resultado.getValor().equals("X")) {
			info.add("Ocurrio un error al intentar operar " + expresion);
		} else {
			resultado.nombre = identificador;
			info.add(numeroDeLinea + "+ Asignacion: En identificador " + identificador + " ~ " + expresion + " = "
					+ resultado.valor);

			ts.replace(identificador, resultado);
			info.add("! L:" + (numeroDeLinea) + "~ Se asigna la variable " + identificador + " : " + resultado.valor);
			ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
		}
//		------------------------------------------------------

	}

	public void leer() {
		String lectura = ctrl.leerLinea(numeroDeLinea);
		if (lectura.isEmpty()) {
			ctrl.alertar("Información", "Ejecución", "Por favor inserta en la consola de entradas en la linea "
					+ numeroDeLinea + " para poder ejecutar la siguiente instruccion");
		} else {
			String tipo = encontrarTipoParaIdentificador(identificadorLectura);
			ArrayList<String> analizador = (new Analizadores()).analizador(true, false, tipo, lectura, numeroDeLinea);
			Variable var = Evaluador.evaluar(Posfijo.postfija(analizador.toArray(new String[0])), ts);
			String valor = Evaluador.convertir(var.getValor(), tipo);
			if (!valor.isEmpty()) {
				asignacionLectura(identificadorLectura, var, lectura);
				indiceDeInstruccion++;
				esperando = false;
				identificadorLectura = "";
			} else {
				info.add("ERROR L" + numeroDeLinea + ": No se puede convertir el dato <" + lectura
						+ "> al tipo de dato " + tipo);
			}
		}
	}

	public void asignacionLectura(String identificador, Variable var, String expresion) {
		/** IMPRESION DEBUG **/
//		System.out.println(Posfijo.separar(expresion));
//		System.out.println(Posfijo.postfija(Posfijo.separar(expresion)));

//		Variable resultado = Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)), ts);

//		------------------------------------------------------
		identificador = identificador.trim();
		if (var.getValor().equals("X")) {
			info.add("Ocurrio un error al intentar operar " + expresion);
		} else {
			var.nombre = identificador;
			info.add(numeroDeLinea + "+ Asignacion: En identificador " + identificador + " ~ " + expresion + " = "
					+ var.valor);

			ts.replace(identificador, var);
			info.add("! L:" + (numeroDeLinea) + "~ Se asigna la variable " + identificador + " : " + var.valor);
			ctrl.añadirCambioEnVariable(identificador, var.valor, numeroDeLinea);
		}
//		------------------------------------------------------

	}

	public boolean evaluarCondicion(String expresion) {
//		Variable resultado = Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)), ts);
//		------------------------------------------------------
		Variable resultado = Evaluador.evaluar(expresion, ts);
		if (resultado.getValor().equals("X")) {
			info.add("Ocurrio un error al intentar operar " + expresion);
			return false;
		} else {
			info.add(numeroDeLinea + "+ Asignacion: Evaluacion a condicion  ~ " + expresion + " = " + resultado.valor);
			if (resultado.getValor().equals("verdadero")) {
				return true;
			} else {
				return false;
			}
		}
//		------------------------------------------------------
	}

	public String encontrarTipoParaIdentificador(String identificador) {
		identificador = identificador.trim();
		if (ts.containsKey(ts.hash(identificador))) {
			return ts.get(ts.hash(identificador)).getTipo();
		} else {
			info.add("ERROR L" + numeroDeLinea + ": No se encontró el identificador " + identificador
					+ " en la tabla de simbolos");
			return "";
		}
	}

	public ArrayList<NodoSecuenciador> getInstrucciones() {
		return instrucciones;
	}

	public void setInstrucciones(ArrayList<NodoSecuenciador> instrucciones) {
		this.instrucciones = instrucciones;
		indiceDeInstruccion = 0;
		numeroDeLinea = 0;
	}

	public void setTS(TablaDeSimbolos ts) {
		this.ts = ts;
	}

	public void imprimirTablaDeSimbolos() {
		System.out.println(ts.ts);
	}

	public boolean estaCorriendo() {
		return corriendo;
	}

	
	
	
}
