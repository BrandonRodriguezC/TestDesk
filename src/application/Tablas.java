package application;

import java.util.ArrayList;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Tablas extends TabPane {
	Tabla manual, automatica;
	Tab TablaManual,TablaAutomatica;
	ArrayList<String> identificadores;
	int numeroDeLineas, tamaño;
	
	public Tablas() {
		manual = new Tabla();
        TablaManual= new Tab("Tabla Manual", manual);
        TablaManual.setClosable(false);
       
        automatica = new Tabla();
        TablaAutomatica= new Tab("Tabla Automatica", automatica);
        TablaAutomatica.setClosable(false);
        
        getTabs().addAll(TablaAutomatica, TablaManual);
        
        tamaño=0;
        widthProperty().addListener((observable, oldValue, newValue) ->
        {
        	manual.actualizarTamaño(newValue.doubleValue());
        	automatica.actualizarTamaño(newValue.doubleValue());
        	
        });
        identificadores= new ArrayList<String>();
	}

	
	public void ActualizarTabla(ArrayList<String> identificadores, int numeroDeLineas) {
		if (identificadores.size()==0) {
			identificadores.add("No hay variables");
		}
		
		if(identificadores.size()!= this.identificadores.size()) {
			manual.limpiar();
			automatica.limpiar();
			manual.columnasNuevas(identificadores, getWidth());
			automatica.columnasNuevas(identificadores, getWidth());
			for (int i = 0; i < numeroDeLineas; i++) {
				manual.agregarFila(false);
				automatica.agregarFila(true);
			}
			this.identificadores=identificadores;
			this.numeroDeLineas=numeroDeLineas;
			/** Limpia todo */
		}else {
			
			if (!identificadores.equals(this.identificadores)) {
				manual.actualizarTitulos(identificadores);
				automatica.actualizarTitulos(identificadores);
				this.identificadores=identificadores;
				/** Actualiza titulos */
			}
			
			if (numeroDeLineas!=this.numeroDeLineas) {
				int diferencia = numeroDeLineas - this.numeroDeLineas;
				if(diferencia > 0) {
					for (int i = 0; i < diferencia; i++) {
						manual.agregarFila(false);
						automatica.agregarFila(true);
					}
					/** Agrega Filas */
				}else {
					diferencia= Math.abs(diferencia);
					for (int i = 0; i < diferencia; i++) {
						manual.eliminarFila();
						automatica.eliminarFila();
					}
					/** Elimina filas */
				}
				this.numeroDeLineas=numeroDeLineas;
			}
		}
	}
	
	public void añadirCambioEnVariable(String nombre, String cambio, int numeroDeLinea) {	
		automatica.cambio(nombre, cambio, numeroDeLinea);
	}
	
	public void comparar() {
		int tamañoContenido = automatica.contenido.getChildren().size();
		int columnas = automatica.tamaño;
		for (int i = columnas; i < tamañoContenido; i++) {
			StackPane  spA = (StackPane) automatica.contenido.getChildren().get(i);
			StackPane  spM = (StackPane) manual.contenido.getChildren().get(i);
			TextField tfA=  (TextField) spA.getChildren().get(1);
			TextField tfM= (TextField) spM.getChildren().get(1);
			if(!tfA.getText().equals(tfM.getText())) {
				tfA.getStyleClass().remove("rectangulo");
				tfM.getStyleClass().remove("rectangulo");
				tfA.getStyleClass().add("discrepancia");
				tfM.getStyleClass().add("discrepancia");
				Rectangle rctA = (Rectangle) spA.getChildren().get(0);
				Rectangle rctM = (Rectangle) spA.getChildren().get(0);
				rctA.getStyleClass().remove("rectangulo");
				rctM.getStyleClass().remove("rectangulo");
				rctA.getStyleClass().add("discrepancia");
				rctM.getStyleClass().add("discrepancia");
			}
		}
	}
	
	public void limpiar() {
		int tamañoContenido = automatica.contenido.getChildren().size();
		int columnas = automatica.tamaño;
		for (int i = columnas; i < tamañoContenido; i++) {
			StackPane  spA = (StackPane) automatica.contenido.getChildren().get(i);
			StackPane  spM = (StackPane) manual.contenido.getChildren().get(i);
			TextField tfA=  (TextField) spA.getChildren().get(1);
			TextField tfM= (TextField) spM.getChildren().get(1);
				tfA.getStyleClass().remove("discrepancia");
				tfM.getStyleClass().remove("discrepancia");
				tfA.getStyleClass().add("rectangulo");
				tfM.getStyleClass().add("rectangulo");
				Rectangle rctA = (Rectangle) spA.getChildren().get(0);
				Rectangle rctM = (Rectangle) spA.getChildren().get(0);
				rctA.getStyleClass().remove("discrepancia");
				rctM.getStyleClass().remove("discrepancia");
				rctA.getStyleClass().add("rectangulo");
				rctM.getStyleClass().add("rectangulo");
				tfA.setText("");
				tfM.setText("");
		}
	}
}
