package application;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.fx.ui.controls.styledtext.StyledTextContent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class InsertMenu extends javafx.scene.control.ContextMenu {

	private final MenuItem ifCM = new MenuItem("Si");
	private final MenuItem ifDCM = new MenuItem("Si ~ Sino");

	private final MenuItem whileCM = new MenuItem("Mientras");
	private final MenuItem forCM = new MenuItem("Repetir");

	private final MenuItem sdlCM = new MenuItem("Salto de linea");

	private final MenuItem enteroDA = new MenuItem("Entero");
	private final MenuItem logicoDA = new MenuItem("Logico");
	private final MenuItem realDA = new MenuItem("Real");
	private final MenuItem textoDA = new MenuItem("Texto");

	private final MenuItem enteroD = new MenuItem("Entero");
	private final MenuItem logicoD = new MenuItem("Logico");
	private final MenuItem realD = new MenuItem("Real");
	private final MenuItem textoD = new MenuItem("Texto");

	private final MenuItem asigCM = new MenuItem("Asignación");

	private final MenuItem todoCM = new MenuItem("Todo");

	private final MenuItem comentarioCM = new MenuItem("Comentario");

	private final MenuItem metodoEscribir = new MenuItem("Escribir");
	private final MenuItem metodoLeer = new MenuItem("Leer");

	private final MenuItem borrar = new MenuItem("Eliminar");

	private final Menu declCM = new Menu("Declaración");
	private final Menu declAsigCM = new Menu("Declaración y asignación");
	private final Menu cicloCM = new Menu("Ciclo");
	private final Menu condicionalCM = new Menu("Condicional");

	private static final String ESCRIBIRstatement = "escribir(      );";
	private static final String LEERstatement = "       = leer( );";

	private static final String declaracionENTEROstatement = "entero      ;";
	private static final String declaracionDECIMALstatement = "real       ;";
	private static final String declaracionTEXTOstatement = "texto       ;";
	private static final String declaracionLOGICOstatement = "logico       ;";

	private static final String ENTEROstatement = "entero       =      ;";
	private static final String DECIMALstatement = "real       =       ;";
	private static final String TEXTOstatement = "texto       =       ;";
	private static final String LOGICOstatement = "logico       =       ;";
	private static final String ASIGNACIONstatement = "       =       ;";
	private static final String SALTODELINEAstatement = "\n";

	private static final String TABsimbolo = "\t";

	private static final String IFstatement = "si (       )";
	private static final String WHILEstatement = "mientras que (       )";
	private static final String FORstatement = "repetir (       ) veces";

	private static final String CORCHETEAsimbolo = "{";
	private static final String CORCHETECsimbolo = "}";

	private static final String ELSEstatement = "sino";

	private static final String COMENTARIOINICIOstatement = "/**\n*";
	private static final String COMENTARIOFINstatement = "\n**/";
	private static final String COMENTARIOstatement = COMENTARIOINICIOstatement + COMENTARIOFINstatement;
	private ArrayList<Integer> rangoBloques;

	public InsertMenu(CodeArea ca) {
		rangoBloques = new ArrayList<Integer>();

		declCM.getItems().addAll(enteroD, logicoD, realD, textoD);
		declAsigCM.getItems().addAll(enteroDA, logicoDA, realDA, textoDA);
		cicloCM.getItems().addAll(whileCM, forCM);
		condicionalCM.getItems().addAll(ifCM, ifDCM);

		/** --------------------------- COMENTARIO ------------------------------- **/

		comentarioCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					String estructura = COMENTARIOstatement;
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		/** --------------------------- CONDICIONALES ----------------------------- **/

		ifCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = (new StringBuilder())
							.append(construirBloque(IFstatement, tab - diferencia)).append(SALTODELINEAstatement)
							.append(construirBloque(CORCHETEAsimbolo, tab))
							.append(SALTODELINEAstatement)
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(CORCHETECsimbolo, tab))
							.toString();
					
					ca.añadirEstructura(estructura);
				}

				
			}
		});

		ifDCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = (new StringBuilder())
							.append(construirBloque(IFstatement, tab - diferencia)).append(SALTODELINEAstatement)
							.append(construirBloque(CORCHETEAsimbolo, tab))
							.append(SALTODELINEAstatement)
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(CORCHETECsimbolo, tab))
							.append(SALTODELINEAstatement).append(construirBloque(ELSEstatement, tab))
							.append(SALTODELINEAstatement).append(construirBloque(CORCHETEAsimbolo, tab))
							.append(SALTODELINEAstatement).append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(CORCHETECsimbolo, tab)).toString();
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		/** --------------------------- CICLOS ----------------------------------- **/

		whileCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = (new StringBuilder())
							.append(construirBloque(WHILEstatement, tab - diferencia)).append(SALTODELINEAstatement)
							.append(construirBloque(CORCHETEAsimbolo, tab))
							.append(SALTODELINEAstatement)
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(CORCHETECsimbolo, tab)).toString();
					
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		forCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = (new StringBuilder())
							.append(construirBloque(FORstatement, tab - diferencia)).append(SALTODELINEAstatement)
							.append(construirBloque(CORCHETEAsimbolo, tab))
							.append(SALTODELINEAstatement)
							.append(construirBloque(SALTODELINEAstatement, tab)).append(construirBloque(SALTODELINEAstatement, tab))
							.append(construirBloque(SALTODELINEAstatement, tab)).append( construirBloque(CORCHETECsimbolo, tab))
							.toString();
					
					ca.añadirEstructura(estructura);
				}
				
				
			}
		});

		/** --------------------------- DECLARACION Y ASIGNACION ----------------- **/

		enteroDA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);

					int diferencia = tab != 0 ? diferencia(linea) : 0;

					String estructura=  construirBloque(ENTEROstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		realDA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura=  construirBloque(DECIMALstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		textoDA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura= construirBloque(TEXTOstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		logicoDA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura=construirBloque(LOGICOstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		/** --------------------------- SALTO DE LINEA ---------------------------- **/

		sdlCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int caret = ca.getCaretOffset();
				int lineNum = ca.getLineAtOffset(caret);
				String linea = ca.getContent().getLine(lineNum);
				if (linea.matches("(\t+)?(si|mientras|repetir).*")) {
					if (caret == ca.getOffsetAtLine(lineNum)) {
						String estructura = SALTODELINEAstatement;
						ca.añadirEstructura(estructura);
					}
				}else if(linea.matches("(\t+)?(\\{).*")){
					if (caret == ca.getOffsetAtLine(lineNum)+ linea.length()) {
						String estructura = SALTODELINEAstatement;
						ca.añadirEstructura(estructura);
					}
				}else if(linea.matches("(\t+)?(\\}).*")) {
					try {
						String siguienteLinea =ca.getContent().getLine(lineNum+1);
						if (!siguienteLinea.contains("sino") && caret == ca.getOffsetAtLine(lineNum)) {
							String estructura = SALTODELINEAstatement;
							ca.añadirEstructura(estructura);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else if(!linea.matches("(\t+)?(sino).*")){
					if (caret == ca.getOffsetAtLine(lineNum)+ linea.length() || caret == ca.getOffsetAtLine(lineNum)) {
						String estructura = SALTODELINEAstatement;
						ca.añadirEstructura(estructura);
					}
				}
				
				
			}
		});

		/** --------------------------- ASIGNACION -------------------------------- **/

		asigCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = construirBloque(ASIGNACIONstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		/** --------------------------- METODOS ----------------------------------- **/

		metodoEscribir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = construirBloque(ESCRIBIRstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		metodoLeer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = construirBloque(LEERstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		/** --------------------------- PLANTILLA --------------------------------- **/

		todoCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					String estructura = 

							ENTEROstatement + SALTODELINEAstatement + 
							DECIMALstatement + SALTODELINEAstatement + 
							TEXTOstatement + SALTODELINEAstatement + 
							LOGICOstatement + SALTODELINEAstatement +
							
							SALTODELINEAstatement +  
							
							declaracionENTEROstatement + SALTODELINEAstatement +
							declaracionDECIMALstatement + SALTODELINEAstatement +
							declaracionLOGICOstatement + SALTODELINEAstatement + 
							declaracionTEXTOstatement + SALTODELINEAstatement + 

							SALTODELINEAstatement +

							ASIGNACIONstatement + SALTODELINEAstatement + 
							
							SALTODELINEAstatement +
							
							LEERstatement+ SALTODELINEAstatement +
							
							ESCRIBIRstatement+ SALTODELINEAstatement +
							
							SALTODELINEAstatement +
							

							IFstatement + SALTODELINEAstatement + CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement + SALTODELINEAstatement + CORCHETECsimbolo

							+ SALTODELINEAstatement + SALTODELINEAstatement

							+ IFstatement + SALTODELINEAstatement + CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement + SALTODELINEAstatement + CORCHETECsimbolo
							+ SALTODELINEAstatement + ELSEstatement + SALTODELINEAstatement + CORCHETEAsimbolo
							+ SALTODELINEAstatement + SALTODELINEAstatement + SALTODELINEAstatement
							+ SALTODELINEAstatement + CORCHETECsimbolo

							+ SALTODELINEAstatement + SALTODELINEAstatement

							+ WHILEstatement + SALTODELINEAstatement + CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement + SALTODELINEAstatement + CORCHETECsimbolo

							+ SALTODELINEAstatement + SALTODELINEAstatement

							+ FORstatement + SALTODELINEAstatement + CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement + SALTODELINEAstatement + CORCHETECsimbolo;
					
					ca.añadirEstructura(estructura);
				}
			}
		});

		/** --------------------------- DECLARACIÓN ------------------------------- **/
		enteroD.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = construirBloque(declaracionENTEROstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		realD.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura= construirBloque(declaracionDECIMALstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
				
			}
		});

		textoD.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura = construirBloque(declaracionTEXTOstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
			}
		});

		logicoD.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				
				if (!linea.contains(";") && !linea.contains("(") && !linea.contains("}") && !linea.contains("{")
						&& !linea.contains("sino")) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String estructura= construirBloque(declaracionLOGICOstatement, tab - diferencia);
					ca.añadirEstructura(estructura);
				}
			}
		});

		borrar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				boolean bloque = false;
				int indice = 0;
				int tamaño = rangoBloques.size();
				String linea = ca.getContent().getLine(ca.getContent().getLineAtOffset(actual));
				StyledTextContent stc = ca.getContent();
				int limiteAceptacion1par = 0, limiteAceptacion2par = 0, limiteAceptacion1impar = 0,
						limiteAceptacion2impar = 0, lineaRango = 0;
				int inicio = 0, fin = 0;
				if (linea.contains("si (") || linea.contains("mientras que (") || linea.contains("repetir (") || linea.contains("sino")
						|| linea.contains("{") || linea.contains("}")) {
					while (!bloque && indice < tamaño) {
						if (indice % 2 == 0) {
							limiteAceptacion1par = stc
									.getOffsetAtLine(stc.getLineAtOffset(rangoBloques.get(indice)) - 1);
							lineaRango = stc.getLineAtOffset(rangoBloques.get(indice));
							limiteAceptacion2par = stc.getOffsetAtLine(lineaRango) + stc.getLine(lineaRango).length();
							if (limiteAceptacion1par <= actual && actual <= limiteAceptacion2par) {
								bloque = true;
								inicio = limiteAceptacion1par;
								int aux = stc.getLineAtOffset(rangoBloques.get(indice + 1));
								fin = stc.getOffsetAtLine(aux) + stc.getLine(aux).length();
								ca.eliminarBloque(inicio, fin);
							}
						} else {
							lineaRango = stc.getLineAtOffset(rangoBloques.get(indice));
							limiteAceptacion1impar = stc.getOffsetAtLine(lineaRango);
							limiteAceptacion2impar = stc.getOffsetAtLine(lineaRango) + stc.getLine(lineaRango).length();
							if (limiteAceptacion1impar <= actual && actual <= limiteAceptacion2impar) {
								bloque = true;
								int aux = stc.getLineAtOffset(rangoBloques.get(indice - 1)) - 1;
								inicio = stc.getOffsetAtLine(aux);
								fin = limiteAceptacion2impar;
								ca.eliminarBloque(inicio, fin);
							}
						}
						indice++;
					}

				} else {
					inicio = ca.getContent().getOffsetAtLine(ca.getLineAtOffset(actual));
					fin = inicio + linea.length();
					if(linea.isEmpty()) {
						fin= inicio+1;
					}
					ca.eliminarBloque(inicio, fin);
				}
				
				ca.limpiar_actualizar();
				
			}
		});

		getItems().addAll(sdlCM, declCM
//				, declAsigCM
				, asigCM, condicionalCM, cicloCM 
//				,todoCM
				, comentarioCM,
				metodoEscribir, metodoLeer, borrar);
	}

	private String construirBloque(String parte, int tab) {
		char tabs [] = new char [tab];
		Arrays.fill(tabs, '\t');
		return (new StringBuilder()).append(new String(tabs)).append(parte).toString();
	}

	public void limpiarRangosBloques() {
		rangoBloques = new ArrayList<Integer>();
	}

	public void insertarRangoBloques(int rango) {
		rangoBloques.add(rango);
	}

	public int diferencia(String linea) {
		int tam = linea.length();
		int i = 0;
		int rta = 0;
		while (i < tam) {
			rta = "\t".equals(linea.charAt(i) + "") ? rta + 1 : rta;
			i++;
		}
		return rta;
	}

	public int tabsAnidamientos(int actual) {
		int tam = rangoBloques.size();
		int i = 0;
		int tabs = 0;
		while (i < tam) {
			int rI = rangoBloques.get(i);
			int rF = rangoBloques.get(i + 1);
			if (actual > rI && actual < rF) {
				tabs++;
			}
			i += 2;
		}
		return tabs;
	}

//	public void imprimirRangos() {
//		for (int i = 0; i < rangoBloques.size(); i++) {
//			System.out.println(rangoBloques.get(i));
//		}
//	}

	public int eliminarUltimoRango() {
		rangoBloques.remove(rangoBloques.size() - 1);
		return rangoBloques.get(rangoBloques.size() - 1);
	}

}
