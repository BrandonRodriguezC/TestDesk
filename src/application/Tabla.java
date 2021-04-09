package application;

import java.awt.Graphics;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Tabla extends ScrollPane{
	ArrayList<String> nombresVariables;
	GridPane contenido;
	Graphics g;
	int tamaño, filas, ancho= 120, objetos, tamañoTab;
	
	public Tabla() {
		filas=0;
		objetos=0;
		contenido= new GridPane();
		setContent(contenido);
	}
	
	public void columnasNuevas(ArrayList<String> nombres, double tamañoTab) {
		nombresVariables= nombres;
		tamaño= nombres.size();
		objetos=0;
		if( tamañoTab/tamaño >= 120 ) {
			/** REVISAR TAMAÑO DE PANEL**/
			ancho= (int) tamañoTab/tamaño ; 
		}
		
		for (int i = 0; i < tamaño; i++) {
			StackPane stack= new StackPane();
			Label lbl= new Label(nombres.get(i));
			lbl.getStyleClass().add("label-titular");
			Rectangle rec = new Rectangle(ancho,16);
			rec.getStyleClass().add("rectangulo-titular");
			stack.getChildren().addAll(rec, lbl);
			contenido.add(stack, i, filas);
			objetos++;
		}
		filas++;
	}

	public void agregarFila( boolean automatica) {
		for (int i = 0; i < tamaño; i++) {
			StackPane stack= new StackPane();
			TextField tf= new TextField();
			Rectangle rec = new Rectangle(ancho,16);
			tf.setPrefWidth(ancho);
			tf.setPrefHeight(14);
			tf.setMaxHeight(14);
			tf.setAlignment(Pos.CENTER);
			if(automatica) {
				tf.setEditable(false);
			}
			tf.getStyleClass().add("rectangulo");
			rec.getStyleClass().add("rectangulo");
			stack.getChildren().addAll(rec, tf);
			contenido.add(stack, i, filas);
			objetos++;
		}
		filas++;
	}
	
	public void eliminarFila() {
		contenido.getChildren().remove(objetos-tamaño, objetos);
		objetos=objetos-tamaño;
	}
	
	public void actualizarTitulos(ArrayList<String> titulosNuevos) {
		for (int i = 0; i < tamaño; i++) {
			StackPane  sp = (StackPane) contenido.getChildren().get(i);
			Label lbl= (Label) sp.getChildren().get(1);
			lbl.setText(titulosNuevos.get(i));
		}
		nombresVariables= titulosNuevos;
	}	
	
	public void cambio (String nombre, String cambio, int linea) {
		int columnaCambio = nombresVariables.indexOf(nombre);
		StackPane stack = (StackPane) contenido.getChildren().get(linea*tamaño+ columnaCambio);
		TextField tf= (TextField) stack.getChildren().get(1);
		tf.setText(cambio);
	}
	
	public void limpiar() {
		objetos=0;
		filas=0;
		contenido.getChildren().clear();
	}

	public int getTamañoTab() {
		return tamañoTab;
	}

	public void setTamañoTab(int tamañoTab) {
		this.tamañoTab = tamañoTab;
	}
	
	public void actualizarTamaño(double tamañoTab) {
		tamaño= nombresVariables.size();
		ancho=120;
		
		if( tamañoTab/tamaño > 120 ) {
			ancho= (int) tamañoTab/tamaño ; 
		}
		
		StackPane  sp; 
		
		for (int i = 0; i < tamaño; i++) {
			sp = (StackPane) contenido.getChildren().get(i);
			((Rectangle) sp.getChildren().get(0)).setWidth(ancho);
		}
		
		for (int i = tamaño; i < objetos; i++) {
			sp = (StackPane) contenido.getChildren().get(i);
			((Rectangle) sp.getChildren().get(0)).setWidth(ancho);;
			((TextField) sp.getChildren().get(1)).setMaxWidth(ancho);
			((TextField) sp.getChildren().get(1)).setPrefWidth(ancho);
			((TextField) sp.getChildren().get(1)).setMinWidth(ancho);
		}
	}
	
	
	
}
