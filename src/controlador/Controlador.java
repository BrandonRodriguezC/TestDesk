package controlador;

import java.util.ArrayList;
import java.util.Arrays;

import application.Consolas;
import application.EditorCodigo;
import application.Tablas;
import logica.Analizadores;
import logica.Ejecutor;
import logica.Secuenciador;

public class Controlador {
	EditorCodigo ec;
	Consolas cs;
	Secuenciador sc;
	Tablas ts;
	Analizadores as;
	Ejecutor ej;

	public Controlador() {
		ec = null;
		cs= null;
		ts = null;
		as = new Analizadores();
		sc = new Secuenciador(this);
		ej= new Ejecutor(this);
	}

	public void añadirNuevaPestañaEC(String nombre) {
		ec.nuevaPestaña(nombre);
	}
	
	public int numeroPestañasEC(){
		return ec.numeroPestañas();
	}
	
	public void presentarErrores(String errores) {
		if (errores.equals("as")) {
			cs.presentarErrores(as.presentarErrores());
		}else {
			cs.presentarErrores(errores);
		}	
	}
	
	public String tomarCodigo() {
		return ec.tomarCodigo();
	}
	
	public void ponerCodigo(String codigo) {
		ec.ponerCodigo(codigo);
	}
	/************************************************************/
	public void secuenciar(String[] codigo, int numeroDeLineas) {
		//System.out.println(Arrays.toString(codigo));
		ej.setTS(as.getTablaDeSimbolos());
		ts.actualizarColumnasTablas(as.getVariables());
		ts.añadirFilas(numeroDeLineas);
		ej.setInstrucciones(sc.secuenciar(codigo));
		//ej.ejecutar(sc.secuenciar(codigo));
	}
	
	public void ejecutarSiguienteInstruccion() {
		ej.ejecutarSiguienteInstruccion();
	}
	
	public void añadirCambioEnVariable(String nombre, String valor, int numeroDeLinea) {
		ts.añadirCambioEnVariable(nombre, valor, numeroDeLinea);
	}
	
	/************************************************************/
	
	public ArrayList<String> tomarIdentificadoresActuales() {
		return ec.tomarIdentificadores();
	}
	
	public void actualizarColumnasTablas(ArrayList<String> identificadores, int numeroDeLineas) {
		ts.actualizarColumnasTablas(identificadores);
		ts.añadirFilas(numeroDeLineas);
	}
	
	
	public void limpiarErrores() {
		as.inicializarListaDeErrores();
	}
	
	public ArrayList<String> getTablaDeSimbolos() {
		return as.getVariables();
	}
	
	public String encontrarTipoParaIdentificador(String expresion) {
		return as.encontrarTipoParaIdentificador(expresion);
	}
	
	public boolean evaluar(boolean compuesta, boolean declaracion, String tipoDeVariable, String expresion, int linea) {
		return as.evaluate( compuesta,  declaracion,  tipoDeVariable,  expresion,  linea);
	}
	
	public void agregarVariable(String nombre, String tipo) {
		as.añadirVariable(nombre, tipo);
	}
	
	public void actualizarVariable(String nombre, String valor) {
		as.actualizarVariable(nombre, valor);
	}
	
	public int numeroDeLineas() {
		return ec.numeroDeLineas();
	}
	
	public void señalarLineaEnCodigo(int numeroDeLinea) {
		ec.señalarLineaEnCodigo(numeroDeLinea);
	}
	
	
	/**------------------------------------------------- GETTER & SETTERS -------------------------------------------------------------**/
	
	public EditorCodigo getEC() {
		return ec;
	}

	public void setEC(EditorCodigo ec) {
		this.ec = ec;
	}

	public Consolas getCS() {
		return cs;
	}

	public void setCS(Consolas cs) {
		this.cs = cs;
	}

	public Tablas getTS() {
		return ts;
	}

	public void setTS(Tablas ts) {
		this.ts = ts;
	}

	public Analizadores getAs() {
		return as;
	}

	public void setAs(Analizadores as) {
		this.as = as;
	}

	public Ejecutor getEj() {
		return ej;
	}

	public void setEj(Ejecutor ej) {
		this.ej = ej;
	}

	
	
}
