package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.fx.ui.controls.styledtext.StyleRange;
import org.eclipse.fx.ui.controls.styledtext.StyledTextArea;
import org.eclipse.fx.ui.controls.styledtext.StyledTextContent;

import controlador.Controlador;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;

public class CodeArea extends StyledTextArea {

	private boolean  error, desarrollador;
	private int ciclos = 0, 
			rangoDeEscrituraInicio1 = 0, rangoDeEscrituraFin1 = 0, 
			rangoDeEscrituraInicio2 = 0, rangoDeEscrituraFin2 = 0, 
			lineaActual = 0, lineaAnterior = -1;
	
	StyleRange[] estilosDeUltimaLinea = null;
	StyleRange[] estiloEjecucion = new StyleRange[1];
	ArrayList<StyleRange> estilos;
	ArrayList<String> informacion;
//	ArrayList<String> estructura;
	int ultimoTamañoNumeroDeRepetir = -1;
	int camposEstilados = 0;


	private StyleRange ARR[];
	private InsertMenu menu;
	private Stack<Integer> pilaBloqueRangos;
	private Controlador ctrl;

	private boolean ejecucion;

	private final static String keyword[] = { "repetir", "veces", "mientras que", "si ", "sino" };
	private final static String tipoDeDato[] = { "entero", "real", "logico", "texto" };
	//private final static String BRACE_PATTERN = "\\(|\\)\n[\t]*\\{|\\) veces\n[\t]*\\{";
	private final static String BRACE_PATTERN = "\\(|\\)";
	private final static String ASIGNACION = "\\=";
	private final static String CIERRE = "\\;";
	private final static String CIERREBLOQUE = "\\}";
	private final static String APERTURABLOQUE = "\\{";
	private static final String COMENTARIO = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
	private static final String ESCRIBIR = "escribir\\(";
	private static final String LEER = "leer\\(\s\\);";
	private final static Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + String.join("|", keyword) + ")"
			+ "|(?<PARENTESIS>" + BRACE_PATTERN + ")" + "|(?<TIPODEDATO>" + String.join("|", tipoDeDato) + ")" + "|(?<LEER>" + LEER + ")"
			+ "|(?<ASIGNACION>" + ASIGNACION + ")" + "|(?<COMENTARIO>" + COMENTARIO + ")" + "|(?<CIERRE>" + CIERRE + ")"
			+ "|(?<CIERREBLOQUE>" + CIERREBLOQUE + ")" + "|(?<APERTURABLOQUE>" + APERTURABLOQUE + ")" + "|(?<ESCRIBIR>"
			+ ESCRIBIR + ")");

	public CodeArea(Controlador ctrl) {

		this.ctrl = ctrl;
		desarrollador=false;
		menu = new InsertMenu(this);
		setContextMenu(menu);
		setLineRulerVisible(true);
		getStyleClass().add("margen");

		setOnKeyPressed(event -> {
			if (dentroLimitesEscritura()) {
				setEditable(true);
//				POR REVISAR
//				if (event.getCode() == KeyCode.BACK_SPACE) {
//					if ((rangoDeEscrituraInicio1 + 1) == (caretOffset - 1)
//							|| (rangoDeEscrituraInicio2 + 1) == (caretOffset - 1)) {
//						setEditable(false);
//					} else {
//						setEditable(true);
//					}
//				}
				if (event.getCode() == KeyCode.ENTER || ((event.getCode() == KeyCode.COMMA) && event.isShiftDown())) {
					setEditable(false);
				} else {
					setEditable(true);
				}
			} else {
				setEditable(false);
			}
		});

//		setOnKeyReleased(event -> {
//			System.out.println("Limpiar desde tecla");
//			limpiar_actualizar();
//		});

		caretOffsetProperty().addListener((observable, oldValue, newValue) -> {
			if (ejecucion==false) {
				Platform.runLater(new Runnable() {
					public void run() {
						limpiar_actualizar();	
					}
				});
			}
			
			
			
		});

		ctrl.presentarErrores(null, "as");
		
	}

	/**
	 * Documentación: revisa si la posición del caretOffset se encuentra dentro de
	 * los limites de escritura
	 **/
	public boolean dentroLimitesEscritura() {
		if (ejecucion) {
			return false;
		}
		int caret = getCaretOffset();
		if (rangoDeEscrituraInicio1 < caret && caret < rangoDeEscrituraFin1) {
			return true;
		} else if (rangoDeEscrituraInicio2 < caret && caret < rangoDeEscrituraFin2) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized void  limpiar_actualizar() {
		
			ctrl.limpiarErrores();
//			System.out.println("------- Se limpia");
			limpiarRangosBloques();
			lineaActual = getContent().getLineAtOffset(getCaretOffset());
			correcionEspacios_AsignacionCamposEscritura();
			lineaAnterior = lineaActual;
			actualizarRangoEscritura();
			actualizarEstilos();
//			ctrl.analizadoresInformacion();
//			POR REVISAR
			ctrl.presentarErrores(null, "as");
			ctrl.actualizarTablas();
//			System.out.println("------- Termino el analisis");
		
		
		
	}
	
	public void actualizarEstilos() {
		ARR = new StyleRange[0];
		StyledTextContent contenido = getContent();
		int numeroLineas = contenido.getLineCount();
		estilos = new ArrayList<StyleRange>();
		pilaBloqueRangos = new Stack<Integer>();
//		System.out.println();
		ciclos=0;
		error = false;
		
//		estructura.add("INICIO");
//		System.out.println("#### Inicio las lineas");
		for (int i = 0; i < numeroLineas; i++) {
			
			estilizarLinea(contenido.getLine(i),i);
			
		}
//		System.out.println("#### Termino las lineas");
		
//		ctrl.presentarEstructura(estructura);
		ARR = estilos.toArray(ARR);
		setStyleRanges(ARR);
	}
		
	public void estilizarLinea(String linea, int numeroLinea) {
		if (!linea.isEmpty()) {
			Matcher comparador = PATTERN.matcher(linea);
			
			int inicioExpresion=-1, finExpresion=-1, tipo_expresion_int = 0;
			String expresion1="", expresion2="";
			String tipo_variable_esperado="";
			
			
			tipo_expresion_int = 
						linea.matches("(\t+)?(\s+)?.*(\s+)?\\=.*\\;") && linea.matches("((?!(entero|logico|real|texto|escribir)).)*") 				? 0://cambiar a [^ ]+ evitar errores de remocion
						linea.matches("(\t+)?(\s+)?(entero|logico|real|texto)(\s+)?.*(\s+)?\\=.*\\;") 										? 1://cambiar a [^ ]+ evitar errores de remocion
						linea.matches("(\t+)?(\s+)?(entero|logico|real|texto)(\s+)?.*(\s+)?\\;") 											? 2://cambiar a [^ ]+ evitar errores de remocion
						linea.matches("(\t+)?(si |mientras que |repetir )(\\().*(\\))(\s+)?(veces)?") || linea.matches("(\t+)?(sino)")		? 3:
						linea.matches("(\t+)?(escribir|leer)(\\().*(\\))\\;")																? 4:
						linea.matches("(\t+)?[\\} | \\{]")																					? 5: -1;
			/**
			 * DICCIONARIO TIPO DE EXPRESION:
			 * 		- ASIGNACION = 0
			 * 		- DECLARACION-ASIGNACION = 1
			 * 		- DECLARACION = 2
			 * 		- BLOQUE = 3
			 * 		- METODO = 4
			 * 		- LLAVE = 5
			 * 		- NO_RECONOCIDO = -1
			 * */
			
			String tipo_expresion_str = tipo_expresion_int == 0 ? "ASIGNACION":
										tipo_expresion_int == 1 ? "DECLARACION-ASIGNACION":
										tipo_expresion_int == 2 ? "DECLARACION":
										tipo_expresion_int == 3 ? "BLOQUE":
										tipo_expresion_int == 4 ? "METODO":
										tipo_expresion_int == 5 ? "LLAVE": "NO_RECONOCIDO";
			
			
			if (tipo_expresion_int != -1) {
//				estructura.add(numeroLinea+"\t|_ "+tipo_expresion_str);
				int offsetLinea = getOffsetAtLine(numeroLinea);
				int asignacion = 0;
				while (comparador.find()) {
					String tipo = comparador.group("TIPODEDATO") != null ? "TIPO_DE_DATO"
							: comparador.group("KEYWORD") != null ? "KEYWORD"
							: comparador.group("PARENTESIS") != null ? "PARENTESIS"
							: comparador.group("LEER") != null ? "LEER"
							: comparador.group("ASIGNACION") != null ? "ASIGNACION"
							: comparador.group("COMENTARIO") != null ? "COMENTARIO"
							: comparador.group("CIERRE") != null ? "CIERRE"
							: comparador.group("CIERREBLOQUE") != null ? "CIERRE_BLOQUE"
							: comparador.group("APERTURABLOQUE") != null ? "APERTURA_BLOQUE"
							: comparador.group("ESCRIBIR") != null ? "ESCRIBIR"
							: null;
					
					if (tipo.equals("KEYWORD")) {
						if (inicioExpresion == -1) {
							estilos.add(new StyleRange("keyword", offsetLinea + comparador.start(), comparador.group().length(), null, null));
//							DEBUG
//							System.out.println("\t\t\t*"+ comparador.group()+" : keyword "+(offsetLinea + comparador.start())+"~"+(offsetLinea + comparador.start()+comparador.group().length()) );
						}
					}else if(tipo.equals("TIPO_DE_DATO")) {
						if (inicioExpresion == -1) {
							inicioExpresion = comparador.end();
							tipo_variable_esperado = Character.toUpperCase(comparador.group().charAt(0)) + "";
							estilos.add(new StyleRange("keyword", offsetLinea + comparador.start(), comparador.group().length(), null, null));
//							DEBUG
//							System.out.println("\t\t\t*"+ comparador.group()+" : keyword "+(offsetLinea + comparador.start())+"~"+(offsetLinea + comparador.start()+comparador.group().length()) );
						}
					}else if(tipo.equals("ASIGNACION")) {
//						
						if (asignacion==0) {
							if (tipo_expresion_int != 3  ) {
								if (tipo_expresion_int != 4) {
									if (inicioExpresion==-1) {
//										System.out.println(tipo_expresion_str+" "+tipo_expresion_int);
										int tabs = (int) linea.chars().filter(ch -> ch == '\t').count();
										inicioExpresion = tabs;
									}
									
									finExpresion = comparador.start();
									expresion1 = linea.substring(inicioExpresion, finExpresion);
									inicioExpresion = comparador.end();
									asignacion++;
								}
							}
						}
					}else if(tipo.equals("CIERRE")) {
						if (tipo_expresion_int!= 4) {
							finExpresion = comparador.start();
							if (expresion1.isEmpty()) {
								expresion1 = linea.substring(inicioExpresion, finExpresion);
							}else {
								expresion2 = linea.substring(inicioExpresion, finExpresion);
							}
							inicioExpresion = -1;
							finExpresion = -1;
						}
					}else if(tipo.equals("APERTURA_BLOQUE")) {
						pilaBloqueRangos.add(offsetLinea+comparador.start());
					}else if(tipo.equals("CIERRE_BLOQUE")) {
						menu.insertarRangoBloques(pilaBloqueRangos.peek());
						menu.insertarRangoBloques(offsetLinea+comparador.end());
						pilaBloqueRangos.pop();
					}else if(tipo.equals("ESCRIBIR")) {
						if (inicioExpresion==-1) {
							estilos.add(new StyleRange("keyword", offsetLinea + comparador.start(), comparador.group().length()-1, null, null));
//							DEBUG
//							System.out.println("\t\t\t*"+ comparador.group()+" : keyword "+(offsetLinea + comparador.start()-1)+"~"+(offsetLinea + comparador.start()+comparador.group().length()) );
						}		
					}else if(tipo.equals("LEER")) {
							inicioExpresion=-1;
							estilos.add(new StyleRange("keyword", offsetLinea + comparador.start(), comparador.group().length(), null, null));
//							DEBUG
//							System.out.println("\t\t\t*"+ comparador.group()+" : keyword "+(offsetLinea + comparador.start()+1)+"~"+(offsetLinea + comparador.start()+comparador.group().length()));
					}		
				}
				
				if (tipo_expresion_int==3 ||tipo_expresion_int==4) {
					if (!linea.contains("leer( );")) {
						StringBuilder sb = new StringBuilder(linea);
						inicioExpresion = sb.indexOf("(");
						finExpresion = sb.lastIndexOf(")");
						if (inicioExpresion != -1 && finExpresion!=-1) {
							expresion1 = linea.substring(inicioExpresion+1, finExpresion);
							if (linea.contains("repetir (") && linea.contains(") veces")) {
								tipo_variable_esperado = "E";
								ciclos++;
								ctrl.agregarVariable("repetir" + ciclos, "E");
							}else if( linea.contains("escribir( ")) {
								tipo_variable_esperado = "T";
							}else {
								tipo_variable_esperado = "L";
							}
						}
					}
				}
				
				if (!expresion1.isEmpty()) {
//					estructura.add(numeroLinea+" \t|\t|__\tExpresion 1: "+ expresion1);
					
					if(tipo_expresion_int == 0) {
						tipo_variable_esperado = ctrl.encontrarTipoParaIdentificador(expresion1.trim());
					}
					boolean resultado= analisisExpresion(tipo_expresion_int, 1, expresion1, tipo_variable_esperado, numeroLinea);
//					System.out.println(expresion1+ " expresion 1 "+numeroLinea);
					estilizarExpresiones(estilos, resultado, numeroLinea);
				}
				if(!expresion2.isEmpty()) {
//					estructura.add(numeroLinea+" \t|\t|__\tExpresion 2: "+ expresion2);
					
					if(tipo_expresion_int == 0|| tipo_expresion_int == 1) {
						tipo_variable_esperado = ctrl.encontrarTipoParaIdentificador(expresion1.trim());
					}
//					System.out.println(expresion1+ " expresion 2: "+numeroLinea );
					boolean resultado= analisisExpresion(tipo_expresion_int, 2, expresion2, tipo_variable_esperado, numeroLinea);
					estilizarExpresiones(estilos, resultado, numeroLinea);
				}
//				estructura.add(numeroLinea+" \t|\t\t");
			}
		}
		
		Collections.sort(estilos, (a, b) -> a.start < b.start ? -1 : a.start == b.start ? 0 : 1);
		camposEstilados=0;
	}
	
	public boolean analisisExpresion(int tipo_expresion, int campo, String expresion, String tipo_variable_esperado, int numero_linea) {
		boolean resultado=false;
		
		/**
		 * DICCIONARIO TIPO DE EXPRESION:
		 * 		- ASIGNACION = 0
		 * 		- DECLARACION-ASIGNACION = 1
		 * 		- DECLARACION = 2
		 * 		- BLOQUE = 3
		 * 		- METODO = 4
		 * 		- LLAVE = 5
		 * 		- NO_RECONOCIDO = -1
		 * */
		
		if (tipo_expresion == 0) {
			if (campo==1) {
				resultado = ctrl.evaluar(false, false, tipo_variable_esperado, expresion, numero_linea, false, false);
			}else {
				resultado = ctrl.evaluar(true, false, tipo_variable_esperado, expresion, numero_linea, false, false);
			}
			
		}else if (tipo_expresion == 1) {
			if (campo==1) {
				resultado = ctrl.evaluar(false, true, tipo_variable_esperado, expresion, numero_linea, false, false);
			}else {
				resultado = ctrl.evaluar(false, false, tipo_variable_esperado, expresion, numero_linea, false, false);
			}
		}else if (tipo_expresion == 2) {
			resultado = ctrl.evaluar(true, true, tipo_variable_esperado, expresion, numero_linea, false, false);
		}else if (tipo_expresion == 3) {
			if (tipo_variable_esperado.equals("E")) {
				resultado = ctrl.evaluar(false, false, tipo_variable_esperado, expresion, numero_linea, true, false);
			}else {
				resultado = ctrl.evaluar(true, false, tipo_variable_esperado, expresion, numero_linea, false, false);
			}
		}else if (tipo_expresion == 4) {
			resultado = ctrl.evaluar(true, false, "T", expresion, numero_linea, false, true);
		}
		if (resultado==false) {
			error=true;
		}
		
		return resultado;
	}

	public void estilizarExpresiones(ArrayList<StyleRange> ar, boolean correctitud, int numeroLineaExpresion) {
		String estilo = "expressionIncorrecta";
		
		if (correctitud) {
			estilo = "expressionCorrecta";
		}
		
		if (!ejecucion && lineaActual == numeroLineaExpresion) {
			if (camposEstilados == 0) {
//				System.out.println("\t\t\tEstilo " + estilo + " " + numeroLineaExpresion + ": " + (rangoDeEscrituraInicio1 + 1) + "-" + (rangoDeEscrituraFin1 - 1));
				ar.add(new StyleRange(estilo, rangoDeEscrituraInicio1 + 1, rangoDeEscrituraFin1 - rangoDeEscrituraInicio1 - 1, null, null));
				camposEstilados++;
				return;
			}
			if (camposEstilados == 1) {
//				System.out.println("\t\t\tEstilo " + estilo + " " + numeroLineaExpresion + ": " + (rangoDeEscrituraInicio2 + 1) + "-" + (rangoDeEscrituraFin2 - 1));
				ar.add(new StyleRange(estilo, rangoDeEscrituraInicio2 + 1, rangoDeEscrituraFin2 - rangoDeEscrituraInicio2 - 1, null, null));
				camposEstilados = 0;
				return;
			}
		}
	}
	
	//*************** por revisar
	public void correcionEspacios_AsignacionCamposEscritura() {
//		System.out.println("---------------------------------------");
		if (!ejecucion) {
			StyledTextContent stc = getContent();
			if (lineaAnterior != lineaActual) {
//				String codigo = getCode(stc);
				ArrayList<String> lineas = new ArrayList<String>();
				int total_lineas= stc.getLineCount();
				for (int i = 0; i < total_lineas; i++) {
					lineas.add(stc.getLine(i));
				}
				
//				System.out.println("Tamaño: "+ lineas.size());
				try {
					lineas.get(lineaAnterior);
				} catch (Exception e) {
					lineaAnterior=-1;
				}
				
				if (lineaAnterior != -1) {
					String ultimaLineaEditada ;
					try {
						ultimaLineaEditada = lineas.get(lineaAnterior);
//						System.out.println("Anterior: "+lineaAnterior+" "+ultimaLineaEditada);
					} catch (Exception e) {
						ultimaLineaEditada="";
					}
					String sinEspacios = ultimaLineaEditada;
					// SI NO CONTIENE TIPO DE DATO ES ASIGNACION
					 
					if (ultimaLineaEditada.matches("((?!(entero|logico|real|texto)).)*")) {
						
						// SI CONTIENE IDENTIFICADOR , REMOVER ESPACIOS ANTES DEL IDENTIFICADOR Y EN EL IGUAL
						
						if (!sinEspacios.matches("^(\t+)?\s+\\=.*")) {
							int tabs = (int) ultimaLineaEditada.chars().filter(ch -> ch == '\t').count();
							char[] tab = new char[tabs];
							Arrays.fill(tab, '\t');
							sinEspacios = (new StringBuilder()).append(sinEspacios.replaceAll("^(\t+)?\s+", new String(tab))).toString();
							sinEspacios = sinEspacios.replaceAll("\s+\\=\s+", " = ");	
						}
						
						if (!sinEspacios.matches(".*\\=\s+\\;$")) {
							sinEspacios = sinEspacios.replaceAll("(\s+)?\\;", ";");
						}
						
					}// SI CONTIENE TIPO DE DATO ES DECLARACION
					else {
						
						//SI CONTIENE IDENTIFICADOR Y PUNTO Y COMA, REMOVER ESPACIOS ENTRE TIPO DE DATO Y PUNTO Y COMA
						String tipo = "entero";
						if (ultimaLineaEditada.contains("logico")) {
							tipo = "logico";
						} else if (ultimaLineaEditada.contains("texto")) {
							tipo = "texto";
						} else if (ultimaLineaEditada.contains("real")) {
							tipo = "real";
						}

						if (ultimaLineaEditada
								.matches("(\t+)?(entero|real|logico|texto)\s+([a-z]+([0-9]+)?)\s*(?:\\,\s*[a-z]+([0-9]+)?)*\s+\\;")) {
							sinEspacios = sinEspacios.replaceAll("(entero|real|logico|texto)\s+", tipo + " ")
									.replaceAll("\s+\\;", ";");
						}
						
						//SI CONTIENE IDENTIFICADOR E IGUAL, REMOVER ESPACIOS ENTRE TIPO DE DATO E IGUAL
//						if (ultimaLineaEditada
//								.matches("(\t+)?(entero|real|logico|texto)(\s+)?([a-z]+([0-9]+)?)(\s+)?\\=")) {
//						 
//							sinEspacios = sinEspacios.replaceAll("(entero|real|logico|texto)\s+", tipo + " ")
//									.replaceAll("\s+\\=\s+", " = ");
//						}

						// SI CONTIENE VALOR, REMOVER ESPACIOS ENTRE IGUAL Y PUNTO Y COMA
//						if (ultimaLineaEditada.matches("\s+\\;")) {
//							sinEspacios = sinEspacios.replaceAll("\s+\\;", ";");
//						}
					}
					lineas.set(lineaAnterior, sinEspacios);
				}
				StringBuilder LineaEnEdicion;
				
				try {
					LineaEnEdicion = new StringBuilder(lineas.get(lineaActual));
				} catch (Exception e) {
					LineaEnEdicion = new StringBuilder();
				}
				
				
				String LineaActual = LineaEnEdicion.toString();
				String conEspacios = LineaActual;
//				System.out.println("Actual: "+lineaActual+" "+LineaActual);
				
				
				
				//SI NO CONTIENE TIPO DE DATO ES ASIGNACION
//				if (LineaActual.matches("(\t+)?(\s+)\\=(\s+)?\\;")) {
//					int tabs = (int) LineaEnEdicion.chars().filter(ch -> ch == '\t').count();
//				}

				if (LineaActual.matches("((?!(entero|logico|real|texto)).)*")) {
					// SI CONTIENE IDENTIFICADOR , AÑADIR ESPACIOS ANTES DEL IDENTIFICADOR Y EN EL IGUAL

					if (LineaActual.matches("(\t+)?(\s+)?([^\s]+)\s+\\=\s+.*\\;")) {
						if (!LineaActual.matches("^(\t+)?\s+\\=.*")) {
//							System.out.println("supone que deberia tener identificador para el cambio");
							int tabs = (int) LineaEnEdicion.chars().filter(ch -> ch == '\t').count();
							char[] tab = new char[tabs];
							Arrays.fill(tab, '\t');
							
							conEspacios = (new StringBuilder()).append(conEspacios.replaceAll("^\t*(\s+)?", new String(tab)+ "   ") .replaceAll("\s+\\=\s+", "   =   ")).toString();
//							System.out.println("Si se reemplazó");
						}
					}
					
					if (LineaActual.matches(".*\\=\s+([^\s]+)\\;")) {
						conEspacios = conEspacios.replace(";", "   ;");
					}

				}
				// SI CONTIENE TIPO DE DATO ES DECLARACION
				else {
					//SI CONTIENE IDENTIFICADOR Y PUNTO Y COMA, AÑADIR ESPACIOS ENTRE TIPO DE DATO Y PUNTO Y COMA
					String tipo = "entero";
					if (LineaActual.contains("logico")) {
						tipo = "logico";
					} else if (LineaActual.contains("texto")) {
						tipo = "texto";
					} else if (LineaActual.contains("real")) {
						tipo = "real";
					}

					if (LineaActual.matches("(\t+)?(entero|real|logico|texto)\s+([a-z]+([0-9]+)?)\s*(?:\\,\s*[a-z]+([0-9]+)?)*\s*\\;")) {
						conEspacios = conEspacios.replaceAll("(entero|real|logico|texto)\s+", tipo + "    ")
								.replaceAll("\\;", "   ;");
					}

					//SI CONTIENE IDENTIFICADOR E IGUAL, AÑADIR ESPACIOS ENTRE TIPO DE DATO E IGUAL
//					if (LineaActual.matches("(\t+)?(entero|real|logico|texto)\s+([a-z]+([0-9]+)?)\s+\\=\s+")) {
//						conEspacios = conEspacios.replaceAll("(entero|real|logico|texto)\s+", tipo + "   ")
//								.replaceAll("\s+\\=\s+", "   =   ");
//					}

					// SI CONTIENE VALOR, AÑADIR ESPACIOS ENTRE IGUAL Y PUNTO Y COMA
//					if (LineaActual.matches("\\;")) {
//						conEspacios = conEspacios.replaceAll("\\;", "   ;");
//					}
				}
				lineas.set(lineaActual, conEspacios);
				
//				codigo = codigo.replace(LineaEnEdicion, conEspacios);
				StringBuilder sb = new StringBuilder(String.join("\n", lineas));
				try {
					while (sb.charAt(sb.lastIndexOf("\n")-1)=='\n') {
						sb.deleteCharAt(sb.lastIndexOf("\n"));
					}
					stc.setText(sb.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	}

	//*****************
	
	public void actualizarRangoEscritura() {
//		estructura = new ArrayList<>();
		
//		estructura.add("################ ESTRUCTURA #################");
		String linea="";
		try {
			linea = getContent().getLine(lineaActual);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		StringBuilder sb = new StringBuilder(linea);
		
		if (linea.matches("(\t+)?(\s+)?.*(\s+)?\\=.*\\;")
				&& linea.matches("((?!(entero|logico|real|texto|escribir)).)*")) {
			
//			estructura.add("ASIGNACION");
			
			int tabs = (int) linea.chars().filter(ch -> ch == '\t').count();
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + tabs, getOffsetAtLine(lineaActual) + sb.indexOf("="), 1);
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + sb.indexOf("="), getOffsetAtLine(lineaActual) + sb.indexOf(";"), 2);
		}else if (linea.matches("(\t+)?(\s+)?(entero|logico|real|texto)(\s+)?.*(\s+)?\\=.*\\;")) {
			
//			estructura.add("DECLARACION Y ASIGNACION");
			
			String tipo = "entero";
			if (linea.contains("real")) {
				tipo = "real";
			} else if (linea.contains("logico")) {
				tipo = "logico";
			} else if (linea.contains("texto")) {
				tipo = "texto";
			}
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + sb.indexOf(tipo) + tipo.length() + 1, getOffsetAtLine(lineaActual) + sb.indexOf("="), 1);
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + sb.indexOf("="), getOffsetAtLine(lineaActual) + sb.indexOf(";"), 2);
		}else if (linea.matches("(\t+)?(\s+)?(entero|logico|real|texto)(\s+)?.*(\s+)?\\;")) {
			
//			estructura.add("DECLARACION");
			
			String tipo = "entero";
			if (linea.contains("real")) {
				tipo = "real";
			} else if (linea.contains("logico")) {
				tipo = "logico";
			} else if (linea.contains("texto")) {
				tipo = "texto";
			}
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + sb.indexOf(tipo) + tipo.length(), getOffsetAtLine(lineaActual) + sb.indexOf(";"), 1);
		}else if (linea.matches("(\t+)?(si |mientras que |repetir )(\\().*(\\))(\s+)?(veces)?")) {
			
//			estructura.add("BLOQUE");
			
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + sb.indexOf("("), getOffsetAtLine(lineaActual) + sb.indexOf(")"), 1);
		}else if (linea.matches("(\t+)?(escribir|leer)(\\().*(\\))\\;")) {
			
//			estructura.add("METODO");
			
			añadirRangoEscritura(getOffsetAtLine(lineaActual) + sb.indexOf("("), getOffsetAtLine(lineaActual) + sb.indexOf(")"), 1);
		}
	}

	public void añadirRangoEscritura(int inicio, int fin, int campo) {
//		estructura.add("\t\tRango de escritura " + lineaActual + " :" + inicio + " - " + fin);
		
		if (campo == 1) {
			rangoDeEscrituraInicio1 = inicio;
			rangoDeEscrituraFin1 = fin;
			rangoDeEscrituraInicio2 = -1;
			rangoDeEscrituraFin2 = -1;
		} else {
			rangoDeEscrituraInicio2 = inicio;
			rangoDeEscrituraFin2 = fin;
		}
	}
	
	public void limpiarRangosBloques() {
		menu.limpiarRangosBloques();
	}

	public ArrayList<String> getTablaDeSimbolos() {
		return ctrl.getTablaDeSimbolos();
	}

	public void añadirEstructura(String statement) {
		StyledTextContent content = getContent();
		String original = getCode(content);
		
		String parte1 = original.substring(0, getCaretOffset());
		String parte2 = original.substring( getCaretOffset());
		
		StringBuilder sb = new StringBuilder();
		sb.append(parte1).append(statement).append(parte2);
//		sb.deleteCharAt(sb.lastIndexOf("\n"));
		
		content.setText(sb.toString());
//		System.out.println("Se limpia desde estructura");
		limpiar_actualizar();
	}
	
	public void eliminarBloque(int inicio, int fin) {
		StringBuffer buf = new StringBuffer(getCode(getContent()));
		buf.replace(inicio, fin, "");
		buf.deleteCharAt(buf.lastIndexOf("\n"));
		getContent().setText(buf.toString());
	}
	
	public String getCode(StyledTextContent content) {
		String original = "";
		int lines = content.getLineCount();
		for (int i = 0; i < lines; i++) {
			original += content.getLine(i) + "\n";
		}
		return original;
	}

	public void señalarLineaEnCodigo(int numeroDeLinea) {
		numeroDeLinea--;
		int offSetNumeroDeLinea = getOffsetAtLine(numeroDeLinea);
		int tamaño = getContent().getLine(numeroDeLinea).length();

		if (numeroDeLinea == 0) {
			estilosDeUltimaLinea = getStyleRanges(numeroDeLinea, tamaño, true);
			lineaAnterior = 0;
		} else {
			int offSetUltimoNumeroDeLinea = getOffsetAtLine(lineaAnterior);
			replaceStyleRanges(offSetUltimoNumeroDeLinea,
					getContent().getLine(getLineAtOffset(offSetUltimoNumeroDeLinea)).length(), estilosDeUltimaLinea);
			estilosDeUltimaLinea = getStyleRanges(offSetNumeroDeLinea, tamaño, true);
			lineaAnterior = numeroDeLinea;
		}

		estiloEjecucion[0] = new StyleRange("ejecucion", offSetNumeroDeLinea, tamaño, null, null);
		replaceStyleRanges(offSetNumeroDeLinea, tamaño, estiloEjecucion);
		setCaretOffset(offSetNumeroDeLinea);
	}
	
	/**
	 * Documentación: En ejecución, actualiza la linea de los ciclos 'repetir' por
	 * el numero de iteraciones restantes
	 **/
	

	public void actualizarRepetir(String numero, int linea) {
		linea = linea - 1;

		StyledTextContent contenido = getContent();
		String textoLinea = contenido.getLine(linea);

		int inicio = getOffsetAtLine(linea);
		int fin = inicio + textoLinea.length();

		String partes[] = textoLinea.split("[0-9]+");
		int tamañoActual = textoLinea.length() - (partes[0].length() + partes[1].length());

		int tamañoNuevo = numero.length();
		StringBuilder lineaFinal = new StringBuilder();

		if (tamañoActual < tamañoNuevo) {
			String partesNuevas[] = textoLinea.split("[0-9]+(\\s{" + (tamañoNuevo - tamañoActual) + "})?");
			lineaFinal.append(partesNuevas[0]).append(numero).append(partesNuevas[1]);
		} else if (tamañoActual > tamañoNuevo) {
			char adicion[] = new char[tamañoActual - tamañoNuevo];
			Arrays.fill(adicion, ' ');
			lineaFinal.append(partes[0]).append(numero).append(new String(adicion)).append(partes[1]);
		} else {
			lineaFinal.append(partes[0]).append(numero).append(partes[1]);
		}

		String codigo = getCode(contenido);
		String primeraParteCodigo = codigo.substring(0, inicio);
		String segundaParteCodigo = codigo.substring(fin);

		StringBuilder sb = new StringBuilder().append(primeraParteCodigo).append(lineaFinal).append(segundaParteCodigo);
		sb.deleteCharAt(sb.lastIndexOf("\n"));
		getContent().setText(sb.toString());
	}
	
	public void ajustarCursor() {
		int last= getContent().getLineCount();
		int off= getContent().getOffsetAtLine(last-1);
//		System.out.println(off);
		setCaretOffset(off);
	}

	/*********************************************** GETTERS Y SETTERS ************************************************/

	public boolean isEjecucion() {
		return ejecucion;
	}

	
	public void setEjecucion(boolean ejecucion) {
		this.ejecucion = ejecucion;
	}
	
	public boolean getError() {
		return error;
	}

	public int getLineaActual() {
		return lineaActual;
	}

	public void setLineaActual(int lineaActual) {
		this.lineaActual = lineaActual;
	}

	public int getLineaAnterior() {
		return lineaAnterior;
	}

	
	public void setLineaAnterior(int lineaAnterior) {
		this.lineaAnterior = lineaAnterior;
	}

	public boolean isDesarrollador() {
		return desarrollador;
	}

	public void setDesarrollador(boolean desarrollador) {
		this.desarrollador = desarrollador;
	}

	
}

