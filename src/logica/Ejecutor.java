package logica;

import java.util.ArrayList;
import java.util.Arrays;

import controlador.Controlador;
import javafx.application.Platform;

public class Ejecutor implements Evaluador, Posfijo {
	TablaDeSimbolos ts;
	Controlador ctrl;
	ArrayList<String> info;
	ArrayList<NodoSecuenciador> instrucciones;
	int indiceDeInstruccion, numeroDeLinea, lineaDeEscritura;
	boolean esperando, corriendo, error;
	String identificadorLectura;
	Thread hilo;

	public Ejecutor(Controlador ctrl) {
		this.ctrl = ctrl;
		info = new ArrayList<String>();
		esperando = false;
		error= false;
	}

	public void ejecutarAutomaticamente(int segundos) {
		if (segundos == -1) {
			segundos = 100;
		} else {
			segundos = segundos * 1000;
		}

		final int segundosF = segundos;

		hilo = new Thread() {
			public void run() {
				NodoSecuenciador actual = instrucciones.get(indiceDeInstruccion);
				corriendo = true;
				while (!actual.getExpresiones().getPrimeraParte().contains("end")) {
					try {
						Thread.sleep(segundosF);
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
		if (error) {
			ctrl.alertar("Error", "Error", "Existe un error en la ejecución de tu codigo, por favor revisa la linea que marca error y corrigela.");
		}else {
			NodoSecuenciador actual;
			if (indiceDeInstruccion < instrucciones.size()) {
				actual = instrucciones.get(indiceDeInstruccion);
			} else {
				actual = instrucciones.get(instrucciones.size() - 1);
			}
			numeroDeLinea = actual.getNumeroDeLinea();
			ctrl.señalarLineaEnCodigo(numeroDeLinea);

			if (esperando) {
				leer();
			} else {
				if (!actual.getExpresiones().getPrimeraParte().contains("end")) {
					if (actual.getExpresiones().getPrimeraParte().equals("jump")) {
						indiceDeInstruccion = actual.getSalto() - 1;
					} else {
						if (actual.getTipo().contains("Declaracion")) {
							if (actual.getExpresiones().getSegundaParte() != null) {
								declaracion(actual.getExpresiones().getTipo(), actual.getExpresiones().getPrimeraParte(),
										actual.getExpresiones().getSegundaParte());
								indiceDeInstruccion++;
							} else {
								String tipo = actual.getExpresiones().getTipo();
								String variables[] = actual.getExpresiones().getPrimeraParte().split(",");
								for (int i = 0; i < variables.length; i++) {
									declaracion(tipo, variables[i].trim());
								}
								indiceDeInstruccion++;
							}
						} else if (actual.getTipo().contains("Asignacion")) {
							asignacion(actual.getExpresiones().getPrimeraParte(),
									actual.getExpresiones().getSegundaParte());
							indiceDeInstruccion++;

						} else if (actual.getTipo().contains("Lectura")) {
							esperando = true;
							identificadorLectura = actual.getExpresiones().getPrimeraParte().trim();
							ctrl.ponerCursor(numeroDeLinea);
						} else if (actual.tipo.contains("Escritura")) {
							Variable v = Evaluador.evaluar(actual.getExpresiones().getPrimeraParte(), ts, numeroDeLinea);
							if (v.getValor().equals("X")) {
								info.add(v.getTipo());
							} else {
								info.add("EJECUCIÓN: "+v.valor);
								ctrl.escribirEnConsola(v.valor, numeroDeLinea);
								indiceDeInstruccion++;
							}
						} else if (actual.getTipo().contains("condicional")) {
							indiceDeInstruccion = evaluarCondicion(actual.getExpresiones().getPrimeraParte())
									? indiceDeInstruccion + 1
									: actual.salto - 1;
						} else if (actual.getTipo().contains("Mientras")) {
							indiceDeInstruccion = evaluarCondicion(actual.getExpresiones().getPrimeraParte())
									? indiceDeInstruccion + 1
									: actual.salto - 1;
						} else if (actual.getTipo().contains("repetir")) {
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
				}else {
					ctrl.deshabilitarSiguienteInstruccion();
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

	public void declaracion(String tipo, String identificador, String expresion) {
		identificador = identificador.trim();
		Variable resultado = Evaluador.evaluar(expresion, ts, numeroDeLinea);
		if (resultado.getValor().equals("X")) {
			info.add(resultado.getTipo());
			
		} else {
			resultado.nombre = identificador;
			if (tipo.equals(resultado.tipo)) {
				ts.put(ts.hash(identificador), resultado);
			} else if (tipo.equals("R") && resultado.tipo.equals("E")) {
				ts.put(ts.hash(identificador), resultado);
			}
			ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
		}

	}

	public void declaracion(String tipo, String identificador) {
		identificador = identificador.trim();
		Variable resultado = new Variable("", tipo, identificador);
		ts.put(ts.hash(identificador), resultado);
		ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
	}

	public void asignacion(String identificador, String expresion) {
		/** IMPRESION DEBUG **/
		identificador = identificador.trim();
//		System.out.println("-------------------------------");
//		System.out.println(expresion);
		Variable resultado = Evaluador.evaluar(expresion, ts, numeroDeLinea);
		if (resultado.getValor().equals("X")) {
			info.add(resultado.getTipo());
		} else {
			
//			System.out.println(ts.get(identificador).getTipo());
//			System.out.println(ts.get(identificador).getTipo().equals("E"));
			
			String TipoInicial= ts.get(identificador).getTipo();
			System.out.println(expresion);
			
			String convertido =	Evaluador.convertirEnAlgoritmo(resultado.valor, TipoInicial);
			
			if (!convertido.isEmpty()) {
				resultado.nombre = identificador;
				resultado.tipo = TipoInicial;
				resultado.valor= convertido;
				ts.replace(identificador, resultado);
				ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
			}else {
				System.out.println("No se pudo convertir "+resultado.valor+" en "+TipoInicial);
			}
			
		}
	}

	public void leer() {
		String lectura = ctrl.leerLinea(numeroDeLinea);
		if (lectura.isEmpty()) {
			ctrl.alertar("Información", "Ejecución", "Por favor inserta en la consola de entradas en la linea "
					+ numeroDeLinea + " para poder ejecutar la siguiente instruccion");
		} else {
			String tipo = encontrarTipoParaIdentificador(identificadorLectura);
			ArrayList<String> analizador = (new Analizadores()).analizador(true, false, tipo, lectura, numeroDeLinea);
			if (analizador != null) {
				Variable var = Evaluador.evaluar(Posfijo.postfija(analizador.toArray(new String[0])), null, numeroDeLinea);
				String valor = Evaluador.convertirEnAlgoritmo(var.getValor(), tipo);
				if (!valor.isEmpty()) {
					asignacionLectura(identificadorLectura, var, lectura);
					indiceDeInstruccion++;
					esperando = false;
					identificadorLectura = "";
				} else {
					info.add("EJECUCIÓN: ERROR L" + numeroDeLinea + "~ LECTURA: No se puede convertir el dato <" + lectura
							+ "> al tipo de dato " + tipo);
				}
			} else {
				info.add("EJECUCIÓN: ERROR L" + numeroDeLinea + "~ LECTURA: No se puede convertir el dato <" + lectura
						+ "> al tipo de dato " + tipo);
			}
			
		}
	}

	public void asignacionLectura(String identificador, Variable var, String expresion) {
		identificador = identificador.trim();
		if (var.getValor().equals("X")) {
			info.add("EJECUCIÓN: Ocurrio un error al intentar operar " + expresion);
		} else {
			var.nombre = identificador;
			ts.replace(identificador, var);
			ctrl.añadirCambioEnVariable(identificador, var.valor, numeroDeLinea);
		}

	}

	public boolean evaluarCondicion(String expresion) {
		Variable resultado = Evaluador.evaluar(expresion, ts, numeroDeLinea);
		if (resultado.getValor().equals("X")) {
			info.add(resultado.getTipo());
			return false;
		} else {
			if (resultado.getValor().equals("verdadero")) {
				return true;
			} else {
				return false;
			}
		}
	}

	public String encontrarTipoParaIdentificador(String identificador) {
		identificador = identificador.trim();
		if (ts.containsKey(ts.hash(identificador))) {
			return ts.get(ts.hash(identificador)).getTipo();
		} else {
			info.add("EJECUCIÓN: ERROR L" + numeroDeLinea + ": No se encontró el identificador " + identificador
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

	public ArrayList<String> getInfo() {
		return info;
	}

	public void setInfo(ArrayList<String> info) {
		this.info = info;
	}

	public boolean isEsperando() {
		return esperando;
	}

	public void setEsperando(boolean esperando) {
		this.esperando = esperando;
	}
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
