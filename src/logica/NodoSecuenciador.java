package logica;

public class NodoSecuenciador {
	int numeroInstruccion,salto, numeroDeLinea;
	String instruccion, tipo;

	public NodoSecuenciador(int numeroInstruccion, String instruccion,  String tipo, int numeroDeLinea) {
		this.numeroInstruccion = numeroInstruccion;
		this.instruccion = instruccion;
		this.salto = 0;
		this.tipo= tipo;
		this.numeroDeLinea= numeroDeLinea+1;
	}

	public int getNumeroInstruccion() {
		return numeroInstruccion;
	}

	public void setNumeroInstruccion(int numeroInstruccion) {
		this.numeroInstruccion = numeroInstruccion;
	}

	public String getInstruccion() {
		return instruccion;
	}

	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	public int getSalto() {
		return salto;
	}

	public void setSalto(int salto) {
		this.salto = salto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNumeroDeLinea() {
		return numeroDeLinea;
	}

	public void setNumeroDeLinea(int numeroDeLinea) {
		this.numeroDeLinea = numeroDeLinea;
	}
	
}
