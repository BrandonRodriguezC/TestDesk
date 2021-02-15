package logica;

import java.util.ArrayList;
import java.util.Stack;

import controlador.Controlador;


public class Secuenciador {
	int count, j, ciclos;
	ArrayList<NodoSecuenciador> list;
	Stack<CierreSecuenciador> stack;
	Controlador ctrl;
	
	
	public Secuenciador(Controlador ctrl) {
		list = new ArrayList<NodoSecuenciador>();
		stack = new Stack<CierreSecuenciador>();
		j = 0;
	    count = 1;
	    this.ctrl=ctrl;
	}

	/** ES POSIBLE AÑADIR LAS VARIABLES DE CICLO DESDE ACA
	 * 	REPERTIR INSTRUCCION -> RI1 ... RIn
	 * 
	 *  AÑADIR NUMERO DE LINEA EN NODO SECUENCIADOR
	 * 
	 * */
	
	public ArrayList<NodoSecuenciador> secuenciar(String vec []) {
		/*** Las siguientes 3 lines estaban al final del metodo**/
		list.clear();
		stack.clear();
		count = 1;
		ciclos=0;
		
		for (int i = 0; i < vec.length; i++) {
			String line= vec[i];
			if (line.contains("sino")) {
				NodoSecuenciador node = new NodoSecuenciador(count, "jump", "condicionalSino", i);
				list.add(node);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains("si")) {
				NodoSecuenciador node = new NodoSecuenciador(count, expresion(line), "condicionalSi", i);
				list.add(node);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains("mientras")) {
				NodoSecuenciador node = new NodoSecuenciador(count, expresion(line), "cicloMientras", i);
				list.add(node);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains("repetir")) {
				ciclos++;
				NodoSecuenciador node = new NodoSecuenciador(count, expresion(line), "repetir"+ciclos, i);
				ctrl.actualizarVariable("repetir"+ciclos , expresion(line));
				list.add(node);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains(";")) {
				if(line.contains("entero")||line.contains("real")||line.contains("texto")||line.contains("logico")) {
					NodoSecuenciador node = new NodoSecuenciador(count,line, "Declaracion", i);
					list.add(node);
					count++;
				}else {
					NodoSecuenciador node = new NodoSecuenciador(count,line, "Asignacion", i);
					list.add(node);
					count++;
				}
				
			} else if (line.contains("}")) {
				// peek?
				int number = stack.get(stack.size() - 1).getNumeroInstruccion();
				String close = stack.get(stack.size() - 1).getCierre();
				if (close.contains("sino")) {
					list.get(number).setSalto(count);
					stack.pop();
				}else if (close.contains("si")) {
					
					if(i+1<list.size() && vec[i+1].contains("sino")) {	
						list.get(number).setSalto(count+1);
					}else {
						list.get(number).setSalto(count+1);
					}
					stack.pop();
				} else if (close.contains("mientras")) {
					NodoSecuenciador node = new NodoSecuenciador(count, "jump", "", i+1);
					node.setSalto(number+1);
					list.add(node);
					list.get(number).setSalto(count + 1);
					stack.pop();
					count++;
				} else if (close.contains("repetir")) {
					list.get(number).setSalto(count + 1);
					stack.pop();
					NodoSecuenciador node = new NodoSecuenciador(count, "jump", "", i+1);
					node.setSalto(number+1);
					list.add(node);
					count++;
				}
			}
		}
		NodoSecuenciador end= new NodoSecuenciador(count, "end", "", vec.length);
		list.add(end);
		ctrl.presentarErrores(textoLista(list));
		return list;
	}
	
	
	
	public String expresion(String linea) {
		int inicio = linea.indexOf("(");
		int fin = linea.indexOf(")");
		//System.out.println("Expresion: "+linea + " I "+ inicio+ " F "+ fin);
		return linea.substring(inicio+1, fin).trim();
		//return linea;	
	}
	
	public void printCode(String vec[]) {
		for (int i = 0; i < vec.length; i++) {
			System.out.println(vec[i]);
		}
	}
	public  String textoLista(ArrayList<NodoSecuenciador> list) {
		NodoSecuenciador node;
		String lista="############################ LISTA INSTRUCCIONES ###########################\n";
		//System.out.println("------------------------- List--------------------------");
		for (int i = 0; i < list.size(); i++) {
			node = list.get(i);
			lista+=(node.getNumeroInstruccion() + "\t" + node.getInstruccion() + "\t" + node.getSalto()+ "\t"+node.getTipo()+"\t"+node.getNumeroDeLinea()+ "\n");
		}
		return lista;
	}
}
