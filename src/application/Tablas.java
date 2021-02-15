package application;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Tablas extends TabPane {
	Tabla manual, automatica;
	public Tablas() {
		manual = new Tabla();
        Tab TablaManual= new Tab("Tabla Manual", manual);
        // En constructor denotar diferencia de tablas 
        automatica = new Tabla();
        Tab TablaAutomatica= new Tab("Tabla Automatica", automatica);
        getTabs().add(TablaManual);
        getTabs().add(TablaAutomatica);
        setPrefWidth(400);;
	}
	
	public void actualizarColumnasTablas(ArrayList<String> identificadores) {
		manual.columnasNuevas(identificadores);
		automatica.columnasNuevas(identificadores);
	}
	
	public void añadirFilas(int numeroDeLineas) {
		for (int i = 0; i < numeroDeLineas; i++) {
			manual.filasNuevas();
			automatica.filasNuevas();
		}
	}
	
	
	public void agregarFilas() {
		int indice = getSelectionModel().getSelectedIndex();
		if (indice==0) {
			manual.filasNuevas();
		}
	}
	
	public void añadirCambioEnVariable(String nombre, String cambio, int numeroDeLinea) {	
		automatica.cambio(nombre, cambio, numeroDeLinea);
	}
}
