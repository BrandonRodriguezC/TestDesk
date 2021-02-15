package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class InsertMenu extends javafx.scene.control.ContextMenu {
	private final MenuItem ifCM = new MenuItem("Si");
	private final MenuItem ifDCM = new MenuItem("Si ~ Sino");

	private final MenuItem whileCM = new MenuItem("Mientras");
	private final MenuItem forCM = new MenuItem("Para");

	private final MenuItem sdlCM = new MenuItem("Salto de linea");

	private final MenuItem entero = new MenuItem("Entero");
	private final MenuItem logico = new MenuItem("Logico");
	private final MenuItem decimal = new MenuItem("Decimal");
	private final MenuItem texto = new MenuItem("Texto");

	private final MenuItem asigCM = new MenuItem("Asignación");
	private final MenuItem todoCM = new MenuItem("Todo");
	private final MenuItem comentarioCM = new MenuItem("Comentario");
	private final MenuItem metodoEscribir = new MenuItem("Escribir");

	private final Menu declCM = new Menu("Declaración");
	private final Menu cicloCM = new Menu("Ciclo");
	private final Menu condicionalCM = new Menu("Condicional");

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
		declCM.getItems().addAll(entero, logico, decimal, texto);
		cicloCM.getItems().addAll(whileCM, forCM);
		condicionalCM.getItems().addAll(ifCM, ifDCM);

		comentarioCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					// ---------------//
					char statement[] = COMENTARIOstatement.toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		ifCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String bloque = construirBloque(IFstatement, tab - diferencia) + SALTODELINEAstatement
							+ construirBloque(CORCHETEAsimbolo, tab) + SALTODELINEAstatement
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(SALTODELINEAstatement, tab)
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(CORCHETECsimbolo, tab);
					char statement[] = bloque.toCharArray();
					ca.addStructures(statement);
				}
			}
		});
		
		ifDCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					
					String bloque = construirBloque(IFstatement, tab - diferencia) + SALTODELINEAstatement
							+ construirBloque(CORCHETEAsimbolo, tab) + SALTODELINEAstatement
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(SALTODELINEAstatement, tab)
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(CORCHETECsimbolo, tab)
							+ SALTODELINEAstatement + construirBloque(ELSEstatement, tab) +SALTODELINEAstatement
							+ construirBloque(CORCHETEAsimbolo, tab) + SALTODELINEAstatement
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(SALTODELINEAstatement, tab)
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(CORCHETECsimbolo, tab);
					char statement[] = bloque.toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		whileCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String bloque = construirBloque(WHILEstatement, tab - diferencia) + SALTODELINEAstatement
							+ construirBloque(CORCHETEAsimbolo, tab) + SALTODELINEAstatement
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(SALTODELINEAstatement, tab)
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(CORCHETECsimbolo, tab);
					char statement[] = bloque.toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		forCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(actual));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					String bloque = construirBloque(FORstatement, tab - diferencia) + SALTODELINEAstatement
							+ construirBloque(CORCHETEAsimbolo, tab) + SALTODELINEAstatement
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(SALTODELINEAstatement, tab)
							+ construirBloque(SALTODELINEAstatement, tab) + construirBloque(CORCHETECsimbolo, tab);
					char statement[] = bloque.toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		entero.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					System.out.println(tab);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					System.out.println(diferencia);
					char statement[] = construirBloque(ENTEROstatement, tab - diferencia).toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		decimal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					char statement[] = construirBloque(DECIMALstatement, tab - diferencia).toCharArray();
					ca.addStructures(statement);
				}
			}
		});
		texto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					char statement[] = construirBloque(TEXTOstatement, tab - diferencia).toCharArray();
					ca.addStructures(statement);
				}
			}
		});
		logico.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					char statement[] = construirBloque(LOGICOstatement, tab - diferencia).toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		
		sdlCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int caret = ca.getCaretOffset();
				int lineNum = ca.getLineAtOffset(caret);
				String linea = ca.getContent().getLine(lineNum);
				if (caret == linea.length() || caret == ca.getOffsetAtLine(lineNum)) {
					char statement[] = SALTODELINEAstatement.toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		asigCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					char statement[] = construirBloque(ASIGNACIONstatement, tab - diferencia).toCharArray();
					ca.addStructures(statement);
				}
			}
		});
		
		metodoEscribir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int actual = ca.getCaretOffset();
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					int tab = tabsAnidamientos(actual);
					int diferencia = tab != 0 ? diferencia(linea) : 0;
					char statement[] = construirBloque("Escribir[   ];", tab - diferencia).toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		todoCM.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String linea = ca.getContent().getLine(ca.getLineAtOffset(ca.getCaretOffset()));
				if (!(linea.matches("\\=|\\(|\\{|\\}"))) {
					String todo = COMENTARIOINICIOstatement + " Esta es la sintaxis para la declaracion \n"
							+ "de variables de tipo ENTERO-LOGICO-DECIMAL-TEXTO" + COMENTARIOFINstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement

							+ ENTEROstatement + SALTODELINEAstatement + DECIMALstatement + SALTODELINEAstatement
							+ TEXTOstatement + SALTODELINEAstatement + LOGICOstatement + SALTODELINEAstatement +

							SALTODELINEAstatement + COMENTARIOINICIOstatement
							+ " Esta es la sintaxis para la asigancion \n" + "de variables" + COMENTARIOFINstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement +

							ASIGNACIONstatement + SALTODELINEAstatement + SALTODELINEAstatement +

							COMENTARIOINICIOstatement + " Esta es la sintaxis para los bloques \n" + " condicionales "
							+ COMENTARIOFINstatement + SALTODELINEAstatement + SALTODELINEAstatement +

							IFstatement + SALTODELINEAstatement
							+ CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement
							+ SALTODELINEAstatement + CORCHETECsimbolo
							
							+SALTODELINEAstatement+SALTODELINEAstatement
							
							+ IFstatement + SALTODELINEAstatement
							+ CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement
							+ SALTODELINEAstatement + CORCHETECsimbolo
							+ SALTODELINEAstatement + ELSEstatement +SALTODELINEAstatement
							+ CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement
							+ SALTODELINEAstatement + CORCHETECsimbolo

							+SALTODELINEAstatement+SALTODELINEAstatement
							
							+COMENTARIOINICIOstatement + " Esta es la sintaxis para los bloques \n" + "ciclicos"
							+ COMENTARIOFINstatement + SALTODELINEAstatement + SALTODELINEAstatement 

							+ WHILEstatement + SALTODELINEAstatement
							+ CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement
							+ SALTODELINEAstatement + CORCHETECsimbolo
							
							+SALTODELINEAstatement+SALTODELINEAstatement
							
							+ FORstatement + SALTODELINEAstatement
							+ CORCHETEAsimbolo + SALTODELINEAstatement
							+ SALTODELINEAstatement + SALTODELINEAstatement
							+ SALTODELINEAstatement + CORCHETECsimbolo;
					char statement[] = todo.toCharArray();
					ca.addStructures(statement);
				}
			}
		});

		getItems().addAll(declCM, asigCM, cicloCM, condicionalCM, sdlCM, todoCM, comentarioCM, metodoEscribir);
	}

	private String construirBloque(String parte, int tab) {
		for (int i = 0; i < tab; i++) {
			parte = TABsimbolo + parte;
		}
		return parte;
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
			int rF = rangoBloques.get(i+1);
			if (actual > rI && actual < rF) {
				tabs++;
			}
			i+=2;
		}

		return tabs;
	}

}
