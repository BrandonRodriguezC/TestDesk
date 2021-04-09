package logica;

public class NodoSecuenciador {
	int numeroInstruccion, salto, numeroDeLinea;
//	String instruccion;
	String tipo;
	ExpresionesLinea expresiones;

	public NodoSecuenciador(int numeroInstruccion, ExpresionesLinea expresiones, String tipo, int numeroDeLinea) {
		this.numeroInstruccion = numeroInstruccion;
		this.expresiones = expresiones;
		this.salto = 0;
		this.tipo = tipo;
		this.numeroDeLinea = numeroDeLinea + 1;
	}

	public int getNumeroInstruccion() {
		return numeroInstruccion;
	}

	public void setNumeroInstruccion(int numeroInstruccion) {
		this.numeroInstruccion = numeroInstruccion;
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

	public ExpresionesLinea getExpresiones() {
		return expresiones;
	}

	public void setExpresiones(ExpresionesLinea expresiones) {
		this.expresiones = expresiones;
	}

	public String toString() {
		
		return (new StringBuilder()).append(this.numeroInstruccion).append("\t|").append(this.numeroDeLinea)
				.append("|\t").append(this.expresiones.getPrimeraParte()).append("\t")
				.append(this.expresiones.getSegundaParte()).append("\t").append(this.salto).append("\t")
				.append(this.tipo).append("\t").append(this.expresiones.getTipo()).append("\n").toString();
	}
}
