package controlador;

import java.util.ArrayList;

import application.BarraSuperior;
import application.Consolas;
import application.EditorCodigo;
import application.Tablas;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
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
	BarraSuperior bs;

	public Controlador() {
		ec = null;
		cs = null;
		ts = null;
		bs = null;
		as = new Analizadores();
		sc = new Secuenciador(this);
		ej = new Ejecutor(this);
	}

	public void añadirNuevaPestañaEC(String nombre) {
		ec.nuevaPestaña(nombre);
	}

	public int numeroPestañasEC() {
		return ec.numeroPestañas();
	}

	public void presentarErrores(ArrayList<String> errores, String origen) {
		if (origen.equals("as")) {
			cs.presentarErrores(as.presentarErrores());
		} else {
			
			cs.presentarErrores(errores);
		}
	}

	public String tomarCodigo() {
		return ec.tomarCodigo();
	}

	public void ponerCodigo(String codigo) {
		limpiarErrores();
		ec.ponerCodigo(codigo);
		presentarErrores(null, "as");
		actualizarTablas();
	}

	public void actualizarTablas() {
		int numeroDeLineas = numeroDeLineas();
		ts.ActualizarTabla(tomarIdentificadoresActuales(), numeroDeLineas);
		cs.actualizarConsolaEntradas(numeroDeLineas);
	}

	public void secuenciar() {
		if (ec.getError() ) {
			alertar("Error", "Existe un error en tu algoritmo", "Tu codigo no compilará hasta que soluciones el error");
		} else if( ej.estaCorriendo()) {
			alertar("Información", "Información", "No puedes detener la ejecucion si se ejecuta en automatico");
		} else {
			if (ec.getEjecucion()) {
				detener();
				
			} else {
				cs.mostrarEscritura();
				ec.setEjecucion(true);
				bs.cambiarIconoEjecucion(true);
				ec.actualizarEstilosEjecucion();
//				System.out.println(as.imprimirExpresionesPosfijas());
				ej.setInfo(new ArrayList<String>());
				ej.setInstrucciones(sc.secuenciar(this.tomarCodigo().replace("\t", "").split("\n"), as.getExpresionesPosfijas()));
				
				ej.setTS(as.getTablaDeSimbolos());
				actualizarTablas();
			}
		}
	}
	
	public void detener() {
		ec.setEjecucion(false);
		bs.cambiarIconoEjecucion(false);
		ec.actualizarEstilosEjecucion();
		ej.setError(false);
		ej.setEsperando(false);
		cs.mostrarInformacion();
		cs.setLineaAnterior(-1);
		ts.setLineaAnterior(-1);
	}

	public void ejecutarSiguienteInstruccion() {
		if (ec.getEjecucion()) {
			ej.ejecutarSiguienteInstruccion();
		} else {
			alertar("Informacion", "Ejecucion", "Por favor pulsa el boton de secuenciar");
		}
	}

	public void alertar(String titulo, String cabecera, String contenido) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecera);
		alert.setContentText(contenido);
		alert.showAndWait();
	}

	public void añadirCambioEnVariable(String nombre, String valor, int numeroDeLinea) {
		ts.añadirCambioEnVariable(nombre, valor, numeroDeLinea);
	}

	public ArrayList<String> tomarIdentificadoresActuales() {
		return ec.tomarIdentificadores();
	}

	public void limpiarErrores() {
		as.inicializarListaDeErrores();
	}

	public ArrayList<String> getTablaDeSimbolos() {
		return as.getVariables();
	}

	public String encontrarTipoParaIdentificador(String expresion, int numeroLinea) {
		return as.encontrarTipoParaIdentificador(expresion, numeroLinea);
	}

	public boolean evaluar(boolean compuesta, boolean declaracionAsignacion, String tipoDeVariable, String expresion,
			int linea, boolean repetir, boolean escribir) {
		return as.evaluate(compuesta, declaracionAsignacion, tipoDeVariable, expresion, linea, repetir, escribir);
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
		ts.señalarLinea(numeroDeLinea);
		cs.señalarLinea(numeroDeLinea);
	}

	public void actualizarRepetir(String numero, int linea) {
		ec.actualizarRepetir(numero, linea);

	}

	public void escribirEnConsola(String expresion, int numeroDeLinea) {
		cs.escribirEnConsola(expresion, numeroDeLinea);
	}

	public String leerLinea(int numeroDeLinea) {
		return cs.leerLinea(numeroDeLinea);
	}

	public void compararTablas() {
		ts.comparar();
	}

	public void limpiarTablas() {
		ts.limpiar();
		cs.limpiar();
	}

//	public void analizadoresInformacion() {
//		as.informacion();
//	}

	public void ejecutarAutomaticamente(int segundos) {
		if (ec.getError()) {
			alertar("Error", "Existe un error en tu algoritmo", "Tu codigo no compilará hasta que soluciones el error");
		} else {
			if (ec.getEjecucion()) {
				ec.setEjecucion(false);
				bs.cambiarIconoEjecucion(false);
				ej.setEsperando(false);
				ec.actualizarEstilosEjecucion();
			} else {
				secuenciar();
				ej.ejecutarAutomaticamente(segundos);
			}
			
		}
		
	}
	
//	public void presentarEstructura(ArrayList<String> estructura) {
//		cs.presentarEstructura(estructura);
//	}
	
	public void setDesarrolladorConsolas() {
		cs.setDesarrollador(!cs.isDesarrollador());
	}
	
//	public void setDesarrolladorEditorCodigo() {
//		ec.setDesarrollador(!ec.isDesarrollador());
//	}
	
	public void ponerCursor(int fila) {
		cs.ponerCursor(fila);
	}
	
	
	public void renombrarEditor(String nombre) {
		ec.renombrarEditor(nombre);
	}
	
	public void ajustarCursor() {
		ec.ajustarCursor();
	}
	
	
	public String tomarNombreDelEditor() {
		return ec.getNombreEditor();
	}
	
	public void deshabilitarSiguienteInstruccion() {
		bs.deshabilitarSiguienteInstruccion();
	}
	
	
	/**
	 * ------------------------------------------------- GETTER & SETTERS
	 * -------------------------------------------------------------
	 **/

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

	public BarraSuperior getBs() {
		return bs;
	}

	public void setBs(BarraSuperior bs) {
		this.bs = bs;
	}

}
