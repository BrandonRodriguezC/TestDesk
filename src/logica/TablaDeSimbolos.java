package logica;

import java.util.ArrayList;
import java.util.Hashtable;

public class TablaDeSimbolos {

	Hashtable<String, Variable> ts;
	ArrayList<String> variables;

	public TablaDeSimbolos() {
		ts = new Hashtable<String, Variable>();
		variables= new ArrayList<String>();
	}

	public String hash(String identificador) {
		String llave = identificador;
//		int tam = identificador.length();
//		for (int i = 0; i < tam; i++) {
//			llave += (int) identificador.charAt(i);
//		}
		return llave;
	}

	public Variable get(String llave) {
		return ts.get(llave);
	}

	public void put(String llave, Variable variable) {
		ts.put(llave, variable);
		variables.add(llave);
	}

	public boolean containsKey(String llave) {
		return ts.containsKey(llave);
	}

	public void replace(String llave, Variable valor) {
		ts.replace(llave, valor);
	}

	/** Documentación: Filtra las variables repetir para su muestra en la tabladesimbolos **/
	public ArrayList<String> elements() {
		ArrayList<String> finalVariables = new ArrayList<String>();
		for (int i = 0; i < variables.size(); i++) {
			if (!variables.get(i).matches("repetir([0-9]+)?")) {
				finalVariables.add(variables.get(i));
			}
		}
		return finalVariables;
	}

	
}
