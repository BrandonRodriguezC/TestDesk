package application;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.fx.ui.controls.styledtext.StyleRange;
import org.eclipse.fx.ui.controls.styledtext.StyledTextArea;
import org.eclipse.fx.ui.controls.styledtext.StyledTextContent;

import controlador.Controlador;
import javafx.scene.input.KeyCode;

public class CodeArea extends StyledTextArea   {
	
	
	private final static String keyword[] = { "repetir", "veces", "mientras que", "si ", "sino"};
	private final static String tipoDeDato[] = { "entero", "decimal", "logico", "texto" };
	private final static String BRACE_PATTERN = "\\(|\\)\n[\t]*\\{|\\) veces\n[\t]*\\{";
	private final static String ASIGNACION = "\\=";
	private final static String CIERRE = "\\;";
	private final static String CIERREBLOQUE = "\\}";
	private final static String APERTURABLOQUE = "\\{";
	
	private static final String COMENTARIO = "//[^\n]*" + "|" +
		 "/\\*(.|\\R)*?\\*/";
	
	private static final String ESCRIBIR="Escribir\\[(.*)\\];";

	private final static Pattern PATTERN = Pattern
			.compile("(?<KEYWORD>" + String.join("|", keyword) + ")" 
					+ "|(?<BRACE>" + BRACE_PATTERN + ")"
					+ "|(?<TIPODEDATO>" + String.join("|", tipoDeDato) + ")"
					+ "|(?<ASIGNACION>" + ASIGNACION + ")" 
					+ "|(?<COMENTARIO>" + COMENTARIO + ")" 
					+ "|(?<CIERRE>" + CIERRE + ")" 
					+ "|(?<CIERREBLOQUE>" + CIERREBLOQUE + ")" 
					+ "|(?<APERTURABLOQUE>" + APERTURABLOQUE + ")" 
					+ "|(?<ESCRIBIR>" + ESCRIBIR + ")" 
					);
	
	private boolean declaracion=false, parentesisDeBloque=false, asignacion=false, keywordBloque=false;
	private int caretOffset = 0, writeRangeS = 0, writeRangeE = 0,count = 0, ciclos=0;
	private StyleRange ARR[];
	private ArrayList<Integer> writeRanges;
	//private Analizadores LEX = new Analizadores();
	//private TextArea consola;
	private String tipoDeDatoEsperado;
	private InsertMenu menu;
	private Stack <Integer> pilaBloqueRangos;

	Controlador ctrl;
	
	public CodeArea(Controlador ctrl) {
		
		this.ctrl= ctrl;
		menu= new InsertMenu(this);
		setContextMenu(menu);
		setLineRulerVisible(true);
		
		setOnKeyPressed(event -> {
				caretOffset = getCaretOffset();
				int check = check();
				if (check != -1) {
					if (event.getCode() == KeyCode.BACK_SPACE) {
						if (caretOffset - writeRanges.get(check) == 0 || caretOffset - writeRanges.get(check + 1) == 0) {
							setEditable(false);
						} else {
							setEditable(true);
						}
					} else {
						setEditable(true);
					}
				} else {
					setEditable(false);
				}
		});
		
		setOnKeyReleased(event -> {
			ctrl.limpiarErrores();
			
			menu.limpiarRangosBloques();
			update();
			
			ctrl.presentarErrores("as");
			//table.columnasNuevas();
			//table.añadirVariable(LEX.getTablaDeSimbolos());
		});
		update();
	}
	
	public ArrayList<String> getTablaDeSimbolos() {
		return ctrl.getTablaDeSimbolos();
	}
	
	public void addStructures(char[] statement) {
		StyledTextContent content = getContent();
		String original = getCode(content);
		char part1[] = original.substring(0, getCaretOffset()).toCharArray();
		char part2[] = original.substring(getCaretOffset()).toCharArray();
		int ifSsize = statement.length;
		int part1size = part1.length;
		int part2size = part2.length;
		int FinalSize = ifSsize + part1size + part2size;
		char Final[] = new char[FinalSize];
		for (int j = 0; j < FinalSize; j++) {
			if (j < (part1size)) {
				Final[j] = part1[j];
			} else if (j >= part1size && j < ifSsize + part1size) {
				Final[j] = statement[j - part1size];
			} else if (j >= part1size + ifSsize && j < part2size + part1size + ifSsize) {
				Final[j] = part2[j - (part1size + ifSsize)];
			}
		}
		String finalText = new String(Final);
		content.setText(finalText);
		ctrl.limpiarErrores();
		update();
		ctrl.presentarErrores("as");
	}
	
	public void update( ) {
		ciclos=0;
		StyledTextContent content= getContent();
		String finalText=getCode(content);
		Matcher matcher = PATTERN.matcher(finalText);
		ArrayList<StyleRange> ar = new ArrayList<StyleRange>();
		writeRanges = new ArrayList<>();
		pilaBloqueRangos = new Stack<Integer>();
		
		ARR = new StyleRange[ar.size()];
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("BRACE") != null ? "brace"
					: matcher.group("TIPODEDATO") != null ? "tipodedato"
					: matcher.group("ASIGNACION") != null ? "asignacion"
					: matcher.group("COMENTARIO") != null ? "comentario"
					: matcher.group("CIERRE") != null ? "cierre"
					: matcher.group("CIERREBLOQUE")!= null ? "cierrebloque"
					: matcher.group("APERTURABLOQUE")!= null ? "aperturabloque"
					: matcher.group("ESCRIBIR")!= null ? "escribir"
							: null;
			/* never happens */ assert styleClass != null;
			
			if (styleClass.equals("tipodedato")) {
				
					if (writeRangeS == 0) {
						writeRangeS = matcher.end()+1;
						writeRanges.add(writeRangeS+1);
						declaracion=true;
						tipoDeDatoEsperado=Character.toUpperCase(matcher.group().charAt(0))+"";
					}
					ar.add(new StyleRange("keyword", matcher.start(), matcher.end() - matcher.start(), null, null));
				
//				System.out.println(" TIPO DE DATO - INICIO: "+ matcher.start() + " FIN : "+ (matcher.end() - matcher.start()));
			} else if (styleClass.equals("keyword") ) {
				keywordBloque=true;
				ar.add(new StyleRange("keyword", matcher.start(), matcher.end() - matcher.start(), null, null));
				
				if (matcher.group().equals("repetir")) {
					tipoDeDatoEsperado= "E";
				}else if (matcher.group().equals("si ") || matcher.group().equals("mientras que")){
					tipoDeDatoEsperado= "L";
				}
//				System.out.println(" KEYWORD - INICIO: "+ matcher.start() + " FIN : "+ (matcher.end() - matcher.start()));
			} else if (styleClass.equals("asignacion")) {
				if(writeRangeS==0) {
					int lineI=content.getLineAtOffset(matcher.start());
					writeRangeS = content.getOffsetAtLine(lineI)+ (int)content.getLine(lineI).chars().filter(ch -> ch== '\t').count();
					writeRanges.add(writeRangeS);
					declaracion=false;
				}
				if (writeRangeE == 0 && parentesisDeBloque==false) {
					writeRangeE = matcher.start() - 2 ;
					writeRanges.add(writeRangeE);
					String expresion= finalText.substring(writeRangeS, writeRangeS + (writeRangeE - writeRangeS));
//					System.out.println("= "+writeRangeS+ " "+ (writeRangeS + (writeRangeE - writeRangeS)) );
					if (!declaracion ) {
						tipoDeDatoEsperado= ctrl.encontrarTipoParaIdentificador(expresion.trim());
					}
					
					boolean lex= ctrl.evaluar(false, declaracion, tipoDeDatoEsperado , expresion,content.getLineAtOffset(writeRangeS)+1 );
					//boolean lex= LEX.evaluate(false, declaracion, tipoDeDatoEsperado , expresion,content.getLineAtOffset(writeRangeS)+1 );
					
					if(lex==true) {
						estilizarExpresiones(ar, "expressionCorrecta");
					}else {
						estilizarExpresiones(ar, "expressionIncorrecta");
					}
					asignacion=true;
					writeRangeS=matcher.end()+1;
					writeRanges.add(writeRangeS);
				}
			} else if(styleClass.equals("cierre")) {
				writeRangeE=matcher.start()-1;
				writeRanges.add(writeRangeE);
//				System.out.println("; "+ writeRangeS+ " "+ (writeRangeS + (writeRangeE - writeRangeS)) );
//				System.out.println(writeRangeS+ " "+(writeRangeS+(writeRangeE - writeRangeS)) );
				boolean lex= ctrl.evaluar(true, false, tipoDeDatoEsperado, finalText.substring(writeRangeS, writeRangeS + (writeRangeE - writeRangeS)), content.getLineAtOffset(writeRangeS)+1);
				//boolean lex= LEX.evaluate(true, false, tipoDeDatoEsperado, finalText.substring(writeRangeS, writeRangeS + (writeRangeE - writeRangeS)), content.getLineAtOffset(writeRangeS)+1);
				if(lex==true) {
					estilizarExpresiones(ar, "expressionCorrecta");
				}else {
					estilizarExpresiones(ar, "expressionIncorrecta");
				}
				writeRangeE=0;
				writeRangeS=0;
				asignacion=false;
				declaracion=false;
			}else if (styleClass.equals("aperturabloque")){
				//System.out.println("ENCONTRO {");
				pilaBloqueRangos.add(matcher.end());
			} else if (styleClass.equals("brace")) {
				if(matcher.group().equals("(") && parentesisDeBloque==false && asignacion==false && keywordBloque==true) {
					if(writeRangeS==0) {
						writeRangeS = matcher.start();
						writeRanges.add(writeRangeS);
						parentesisDeBloque=true;
					}
				}else if(!matcher.group().equals("(") && keywordBloque==true){
						writeRangeE = matcher.start();
						writeRanges.add(writeRangeE);
						String linea=finalText.substring(writeRangeS+1, writeRangeS + (writeRangeE - writeRangeS));
						tipoDeDatoEsperado = (tipoDeDatoEsperado!= null)? tipoDeDatoEsperado: "R";
						/**------------------------------------*/
						pilaBloqueRangos.add(matcher.end());
						
						boolean lex= ctrl.evaluar(true, false, tipoDeDatoEsperado ,linea , content.getLineAtOffset(writeRangeS)+1 );
//						boolean lex = LEX.evaluate(true, false, tipoDeDatoEsperado ,linea , content.getLineAtOffset(writeRangeS)+1 );
						if(lex==true) {
							estilizarExpresiones(ar, "expressionCorrecta");
						}else {
							estilizarExpresiones(ar, "expressionIncorrecta");
						}
						if(matcher.group().contains("veces")) {
							ciclos++;
							ctrl.agregarVariable("repetir"+ciclos, "E");
							//LEX.añadirVariable("ciclol"+(content.getLineAtOffset(writeRangeS)+1), 'E');
							ar.add(new StyleRange("keyword", matcher.start()+2, 5, null, null));
//							System.out.println(" KEYWORD VECES - INICIO: "+ (matcher.start()+2) + " FIN : "+ (matcher.start()+7));
						}
						writeRangeS = 0;
						writeRangeE = 0;
						parentesisDeBloque=false;
						keywordBloque= false;
				}
			}else if (styleClass.equals("cierrebloque")){
				menu.insertarRangoBloques(pilaBloqueRangos.peek());
				menu.insertarRangoBloques(matcher.start());
				pilaBloqueRangos.pop();
			}else if (styleClass.equals("comentario")){
				writeRanges.add(matcher.start()+3);
				writeRanges.add(matcher.end()-3);
				ar.add(new StyleRange("comentario", matcher.start(), matcher.end() - matcher.start(), null, null));
			}else if (styleClass.equals("escribir")) {
				//writeRanges.add(matcher.start()+9);
				//writeRanges.add(matcher.end()-2);
				//ar.add(new StyleRange("escribirLimite", matcher.start(), 9, null, null));
				System.out.println(matcher.group()+ " I: "+matcher.start()+ " F: "+ (matcher.start()-matcher.end()));
				ar.add(new StyleRange("escribir", matcher.start(),matcher.end() - matcher.start() , null, null));
				//ar.add(new StyleRange("escribirLimite", matcher.end()-2, 2, null, null));
			}
		}
		ARR = ar.toArray(ARR);
		this.setStyleRanges(ARR);
	}
	
	public void estilizarExpresiones(ArrayList<StyleRange> ar, String estilo) {
		ar.add(new StyleRange(estilo+"Limite", writeRangeS, 1, null, null));
		ar.add(new StyleRange(estilo, writeRangeS + 1, writeRangeE - writeRangeS - 1, null, null));
		ar.add(new StyleRange(estilo+"Limite", writeRangeE, 1, null, null));
	}

	public int check() {
		int size = writeRanges != null ? writeRanges.size() : 0;
		int i = 0;
		while (i < size) {
			if (caretOffset >= writeRanges.get(i) && caretOffset < writeRanges.get(i + 1)) {
				return i;
			}
			i += 2;
		}
		return -1;
	}

	public String getCode(StyledTextContent content) {
		String original = "";
		int lines = content.getLineCount();
		for (int i = 0; i < lines; i++) {
			original += content.getLine(i) + "\n";
		}
		return original;
	}
	public int numeroDeLineas() {
		return getContent().getLineCount();
	}
	
	//poner caret en linea
	public void count() {
		setCaretOffset(getOffsetAtLine(count));
		count = (count <= (getContent().getLineCount() - 1)) ? count + 1 : 0;
	}
	
	public void señalarLineaEnCodigo(int numeroDeLinea) {
		setCaretOffset(getOffsetAtLine(numeroDeLinea));
	}

	
	
}
