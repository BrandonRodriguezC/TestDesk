package logica;

import java.util.ArrayList;
import java.util.Arrays;

import controlador.Controlador;


public class Ejecutor implements Evaluador, Posfijo{
	Registro rt;
	TablaDeSimbolos ts;
	Controlador ctrl;
	String info;
	ArrayList<NodoSecuenciador> instrucciones;
	int indiceDeInstruccion, numeroDeLinea;
	
	public Ejecutor(Controlador ctrl) {
		rt= new Registro();
		//ts= new TablaDeSimbolos();
		this.ctrl=ctrl;
		info="";
	}
	
	public void ejecutar(ArrayList<NodoSecuenciador> instrucciones) {
		int indice=0;
		NodoSecuenciador actual = instrucciones.get(indice);
		//ts= new TablaDeSimbolos();
		info="================ INFORMACION ================\n";
		
		while(!actual.instruccion.contains("end")) {
			if(actual.instruccion.equals("jump")) {
				indice= actual.salto-1;
			}else {
				if (actual.tipo.contains("Declaracion")) {
					String declaracion[] = actual.instruccion.split("=");
					String primeraParte[] = declaracion[0].trim().replaceAll(" +", " ").split(" ");
					declaracion[1]=declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
					declaracion(Character.toUpperCase(primeraParte[0].charAt(0))+ "",primeraParte[1],declaracion[1] );
					indice++;
				}else if(actual.tipo.equals("Asignacion")) {
					String declaracion[]= actual.instruccion.split("=");
					declaracion[0]=declaracion[0].trim().replaceAll(" +", " ");
					declaracion[1]=declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
					asignacion(declaracion[0],declaracion[1]);
					indice++;
				}else if(actual.tipo.contains("condicional")) {
					indice= evaluarCondicion(actual.instruccion)? indice+1: actual.salto-1;
				}else if(actual.tipo.contains("Mientras")) {
					indice= evaluarCondicion(actual.instruccion)? indice+1: actual.salto-1;
				}else if(actual.tipo.contains("repetir")) {
					if (Integer.parseInt(actual.instruccion)==0) {
						actual.instruccion = ts.get(ts.hash(actual.tipo)).valor;
						indice=  actual.salto-1;
					}else {
						actual.instruccion=(Integer.parseInt(actual.instruccion)-1) + "";
						indice++;
					}
					
				}
			}
			
			actual= instrucciones.get(indice);
		}
		ctrl.presentarErrores(info);
	}
	
	
	public void ejecutarSiguienteInstruccion() {
		NodoSecuenciador actual;
		if(indiceDeInstruccion<instrucciones.size()) {
			 actual = instrucciones.get(indiceDeInstruccion);
		}else {
			actual = instrucciones.get(instrucciones.size()-1);
		}
		numeroDeLinea= actual.getNumeroDeLinea();
		ctrl.señalarLineaEnCodigo(numeroDeLinea-1);
		//ts= new TablaDeSimbolos();
		info="================ INFORMACION ================\n";
		if(!actual.instruccion.contains("end")) {
			if(actual.instruccion.equals("jump")) {
				indiceDeInstruccion= actual.salto-1;
			}else {
				if (actual.tipo.contains("Declaracion")) {
					
					String declaracion[] = actual.instruccion.split("=");
					String primeraParte[] = declaracion[0].trim().replaceAll(" +", " ").split(" ");
					declaracion[1]=declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
					declaracion(Character.toUpperCase(primeraParte[0].charAt(0))+ "",primeraParte[1],declaracion[1] );
					info+=("+ L:"+(numeroDeLinea)+"~ Se declara la variable "+ primeraParte[1]+ " : "+ declaracion[1]+ "\n");
					indiceDeInstruccion++;
				}else if(actual.tipo.equals("Asignacion")) {
					String declaracion[]= actual.instruccion.split("=");
					declaracion[0]=declaracion[0].trim().replaceAll(" +", " ");
					declaracion[1]=declaracion[1].trim().replaceAll(" +", " ").replace(";", "");
					asignacion(declaracion[0],declaracion[1]);
					info+=("+ L:"+(numeroDeLinea)+"~ Se declara la variable "+ declaracion[0]+ " : "+ declaracion[1]+ "\n");
					indiceDeInstruccion++;
				}else if(actual.tipo.contains("condicional")) {
					indiceDeInstruccion= evaluarCondicion(actual.instruccion)? indiceDeInstruccion+1: actual.salto-1;
				}else if(actual.tipo.contains("Mientras")) {
					indiceDeInstruccion= evaluarCondicion(actual.instruccion)? indiceDeInstruccion+1: actual.salto-1;
				}else if(actual.tipo.contains("repetir")) {
					if (Integer.parseInt(actual.instruccion)==0) {
						actual.instruccion = ts.get(ts.hash(actual.tipo)).valor;
						indiceDeInstruccion=  actual.salto-1;
					}else {
						actual.instruccion=(Integer.parseInt(actual.instruccion)-1) + "";
						indiceDeInstruccion++;
					}
					
				}
			}
		}
		ctrl.presentarErrores(info);
	}
	
	public void declaracion(String tipo, String identificador , String expresion) {
//		String[] separado = Posfijo.separar(expresion);
//		String posfija= Posfijo.postfija(separado);
//		System.out.println("---------------------------------");
//		System.out.println(Arrays.toString(separado));
//		System.out.println("Ejecutor: "+posfija);
		
		Variable resultado =  Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)),ts);
		resultado.nombre= identificador;
		if(tipo.equals(resultado.tipo)) {
			ts.put(ts.hash(identificador), resultado);
			info+=("+ L:"+(numeroDeLinea)+"~ Se declara la variable "+ identificador+ " : "+ resultado.valor+ "\n");
		}
		ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
	}
	
	public void asignacion(String identificador , String expresion) {
//		String[] separado = Posfijo.separar(expresion);
//		String posfija= Posfijo.postfija(separado);
//		System.out.println("---------------------------------");
//		System.out.println(Arrays.toString(separado));
//		System.out.println("Ejecutor: "+posfija);
		
		Variable resultado = Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)),ts);
		resultado.nombre= identificador;
		ts.replace(ts.hash(identificador),resultado);
		info+=("! L:"+(numeroDeLinea)+"~ Se asigna la variable "+ identificador+ " : "+ resultado.valor+ "\n");
		ctrl.añadirCambioEnVariable(identificador, resultado.valor, numeroDeLinea);
	}
	
	public boolean evaluarCondicion(String expresion) {
//		String[] separado = Posfijo.separar(expresion);
//		String posfija= Posfijo.postfija(separado);
//		System.out.println("---------------------------------");
//		System.out.println(Arrays.toString(separado));
//		System.out.println("Ejecutor: "+posfija);
		
		Variable resultado = Evaluador.evaluar(Posfijo.postfija(Posfijo.separar(expresion)),ts);
		info+=("? L:"+(numeroDeLinea)+"~ La expresion tuvo resultado "+ resultado.valor+ "\n");
		if(resultado.getValor().equals("true")) {
			return true;
		}else {
			return false;
		}	
	}
	
	public ArrayList<NodoSecuenciador> getInstrucciones() {
		return instrucciones;
	}

	public void setInstrucciones(ArrayList<NodoSecuenciador> instrucciones) {
		this.instrucciones = instrucciones;
		indiceDeInstruccion = 0;
		numeroDeLinea= 0;
	}

	public void setTS(TablaDeSimbolos ts) {
		this.ts=ts;
	}
}
