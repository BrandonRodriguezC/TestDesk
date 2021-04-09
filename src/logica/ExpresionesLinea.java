package logica;

public class ExpresionesLinea {
	String primeraParte, segundaParte, tipo;

	public ExpresionesLinea(String primeraParte) {
		this.primeraParte = primeraParte;
		this.segundaParte = null;
		this.tipo = null;
	}
	

	public String getPrimeraParte() {
		return primeraParte;
	}

	public void setPrimeraParte(String primeraParte) {
		this.primeraParte = primeraParte;
	}

	public String getSegundaParte() {
		return segundaParte;
	}

	public void setSegundaParte(String segundaParte) {
		this.segundaParte = segundaParte;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String toString() {
		String segundaParte = this.segundaParte != null ? this.segundaParte : "No existe";
		String tipo = this.tipo != null ? this.tipo : "No existe";
		return (new StringBuilder()).append("\tPrimera parte: ").append(this.primeraParte).append("\t\t\tSegunda Parte: ").append(segundaParte).append("\tTipo: ").append(tipo)
				.append("\t\n").toString();
	}

}
