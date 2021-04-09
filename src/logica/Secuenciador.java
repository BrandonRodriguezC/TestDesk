package logica;

import java.util.ArrayList;
import java.util.Hashtable;
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
		this.ctrl = ctrl;
	}

	/**
	 * REFORMAR NODO SECUENCIADOR - TOMAR EXPRESIONES POSFIJAS DE ANALIZADORES -
	 * QUITAR POSFIJA DE PYTHON
	 */

	public ArrayList<NodoSecuenciador> secuenciar(String vec[], Hashtable<Integer, ExpresionesLinea> expresionesLinea) {
		list.clear();
		stack.clear();
		count = 1;
		ciclos = 0;
		
		for (int i = 0; i < vec.length; i++) {
			String line = vec[i];
			if (line.contains("sino")) {
				
				/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
				NodoSecuenciador nodo = new NodoSecuenciador(count, new ExpresionesLinea("jump"), "", i);
				list.add(nodo);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains("si")) {
				
				/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
				
				NodoSecuenciador nodo = new NodoSecuenciador(count, expresionesLinea.get(i), "condicionalSi", i);
				list.add(nodo);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains("mientras")) {
				
				/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
				NodoSecuenciador nodo = new NodoSecuenciador(count, expresionesLinea.get(i), "cicloMientras", i);
				list.add(nodo);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains("repetir")) {
				
				/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
				
				ciclos++;
				NodoSecuenciador node = new NodoSecuenciador(count, expresionesLinea.get(i), "repetir" + ciclos, i);
				System.out.println(i+1);
				ctrl.actualizarVariable("repetir" + ciclos, expresionesLinea.get(i).getPrimeraParte().trim());
				
				list.add(node);
				CierreSecuenciador close = new CierreSecuenciador(list.size() - 1, line);
				stack.push(close);
				count++;
			} else if (line.contains(";")) {
				
				if (line.contains("entero") || line.contains("real") || line.contains("texto")
						|| line.contains("logico")) {
					
					/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
					
					NodoSecuenciador node = new NodoSecuenciador(count, expresionesLinea.get(i), "Declaracion", i);
					list.add(node);
					count++;
				} else if (line.contains("escribir(")){
					NodoSecuenciador node = new NodoSecuenciador(count, expresionesLinea.get(i), "Escritura", i);
					list.add(node);
					count++;
				} else if (line.contains("leer(")){
					NodoSecuenciador node = new NodoSecuenciador(count, expresionesLinea.get(i), "Lectura", i);
					list.add(node);
					count++;
				}else {
					
					/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
					
					NodoSecuenciador node = new NodoSecuenciador(count, expresionesLinea.get(i), "Asignacion", i);
					list.add(node);
					count++;
				}

			} else if (line.contains("}")) {
				// peek?
				int number = stack.peek().getNumeroInstruccion();
				String close = stack.peek().getCierre();
				if (close.contains("sino")) {
					list.get(number).setSalto(count);
					stack.pop();
				} else if (close.contains("si")) {

					if (i + 1 < list.size() && vec[i + 1].contains("sino")) {
						list.get(number).setSalto(count + 1);
					} else {
						list.get(number).setSalto(count );
					}
					
					stack.pop();
				} else if (close.contains("mientras")) {
					
					/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
					
					NodoSecuenciador node = new NodoSecuenciador(count,  new ExpresionesLinea("jump"), "", i);
					node.setSalto(number + 1);
					list.add(node);
					list.get(number).setSalto(count + 1);
					stack.pop();
					count++;
				} else if (close.contains("repetir")) {
					
					/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
					
					list.get(number).setSalto(count + 1);
					stack.pop();
					NodoSecuenciador node = new NodoSecuenciador(count, new ExpresionesLinea("jump"), "", i);
					node.setSalto(number + 1);
					list.add(node);
					count++;
				}
			}
		}
		
		/** AÑADIR EXPRESION DESDE EXPRESIONES LINEA **/
		
		NodoSecuenciador end = new NodoSecuenciador(count,  new ExpresionesLinea("end"), "", vec.length-1);
		list.add(end);
		ctrl.presentarErrores(textoLista(list), "sc");
		return list;
	}

//	public void printCode(String vec[]) {
//		for (int i = 0; i < vec.length; i++) {
//			System.out.println(vec[i]);
//		}
//	}

	public ArrayList<String> textoLista(ArrayList<NodoSecuenciador> list) {
		NodoSecuenciador node;
		ArrayList<String> lista = new ArrayList<String>();
		lista.add("############################ LISTA INSTRUCCIONES ###########################\n");
		lista.add("#Instruccion\t#Linea\tPrimera Parte\tSegunda Parte\tSalto\tTipo\n");
		for (int i = 0; i < list.size(); i++) {
			node = list.get(i);
			lista.add(node.toString());
		}
		return lista;
	}
}
