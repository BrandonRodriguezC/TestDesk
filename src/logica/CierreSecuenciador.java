package logica;

public class CierreSecuenciador {
	int numeroInstruccion;
	String cierre;
	
	public CierreSecuenciador(int numeroInstruccion,String cierre) {
		this.numeroInstruccion=numeroInstruccion;
		this.cierre=cierre;
	}

	public int getNumeroInstruccion() {
		return numeroInstruccion;
	}

	public void setNumeroInstruccion(int numeroInstruccion) {
		this.numeroInstruccion = numeroInstruccion;
	}

	public String getCierre() {
		return cierre;
	}

	public void setCierre(String cierre) {
		this.cierre = cierre;
	}
	
	
}
