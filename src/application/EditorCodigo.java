package application;

import java.util.ArrayList;
import java.util.Iterator;

import controlador.Controlador;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class EditorCodigo extends TabPane {
	ArrayList<CodeArea> areasDeCodigo;
	
	Controlador ctrl;

	public EditorCodigo(Controlador ctrl) {
		this.ctrl = ctrl;
		CodeArea areaCodigo = new CodeArea(ctrl);
		areasDeCodigo = new ArrayList<CodeArea>();
		Tab first = new Tab("main.td", areaCodigo);
		
		first.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				actualizarAreasDeCodigo();
			}
		});
		
		areasDeCodigo.add(areaCodigo);
		getTabs().add(first);
		setPrefWidth(400);
	}

	public String tomarCodigo() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		return aux.getCode(aux.getContent());
	}

	public int numeroDeLineas() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		return aux.getContent().getLineCount();
	}

	public void ponerCodigo(String codigo) {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		aux.getContent().setText(codigo);
		aux.limpiar_actualizar();
	}

	public int numeroPestañas() {
		return getTabs().size();
	}
	
	public void actualizarAreasDeCodigo() {
		ObservableList<Tab> lista = getTabs();
		int tamaño = lista.size();
		areasDeCodigo.clear();
		for (int i = 0; i < tamaño; i++) {
			areasDeCodigo.add((CodeArea)lista.get(i).getContent());
		}
	}

	public void nuevaPestaña(String nombre) {
		CodeArea areaCodigo = new CodeArea(ctrl);
		areasDeCodigo.add(areaCodigo);
		Tab tab = new Tab(nombre, areaCodigo);
		
		tab.setOnClosed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				actualizarAreasDeCodigo();
			}
		});
		getTabs().add(tab);
		getSelectionModel().select(tab);
	}

	public ArrayList<String> tomarIdentificadores() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		return aux.getTablaDeSimbolos();
	}

	public void señalarLineaEnCodigo(int numeroDeLinea) {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		aux.señalarLineaEnCodigo(numeroDeLinea);
	}

	public void setEjecucion(boolean ejecucion) {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		aux.setEjecucion(ejecucion);
	}

	public boolean getEjecucion() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		return aux.isEjecucion();
	}

	public void actualizarEstilosEjecucion() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		aux.limpiar_actualizar();
	}

	public void actualizarRepetir(String numero, int linea) {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		aux.actualizarRepetir(numero, linea);
	}
	
	public boolean getError() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		return aux.getError();
	}
	
	public void renombrarEditor(String nombre) {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		getTabs().get(indiceCodigoArea).setText(nombre);
		
	}
	
	public void ajustarCursor() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		aux.ajustarCursor();
	}
	
	public String getNombreEditor() {
		int indiceCodigoArea = getSelectionModel().getSelectedIndex();
		return getTabs().get(indiceCodigoArea).getText();
	}
	
}
