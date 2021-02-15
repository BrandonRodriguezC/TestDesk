package logica;

import java.util.Enumeration;
import java.util.Hashtable;

public class TablaDeSimbolos {

	Hashtable<String, Variable> ts;

	public TablaDeSimbolos() {
		ts = new Hashtable<String, Variable>();
	}

	public String hash(String identificador) {
		String llave = "";
		int tam = identificador.length();
		for (int i = 0; i < tam; i++) {
			llave += (int) identificador.charAt(i);
		}
		return llave;
	}

	public Variable get(String llave) {
		return ts.get(llave);
	}

	public void put(String llave, Variable valor) {
		ts.put(llave, valor);
	}

	public boolean containsKey(String llave) {
		return ts.containsKey(llave);
	}

	public void replace(String llave, Variable valor) {
		ts.replace(llave, valor);
	}

	public Enumeration<Variable> elements() {
		return ts.elements();
	}

	
}
