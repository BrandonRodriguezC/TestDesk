package application;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Tabla extends ScrollPane{
	ArrayList<String> nombresVariables;
	ArrayList<StackPane> Objetos;
	GridPane contenido;
	int tamaño, filas;
	
	public Tabla() {
		filas=0;
		Objetos = new ArrayList<>();
		contenido= new GridPane();
		setContent(contenido);
	}
	
	public void columnasNuevas(ArrayList<String> nombres) {
		nombresVariables= nombres;
		tamaño= nombres.size();
		getChildren().removeAll(Objetos);
		Objetos = new ArrayList<>();
		filas=0;
		contenido.getChildren().clear();
		
		for (int i = 0; i < tamaño; i++) {
			StackPane stack= new StackPane();
			Label lbl= new Label(nombres.get(i));
			lbl.getStyleClass().add("label-titular");
			Rectangle rec = new Rectangle(400/tamaño,16);
			rec.getStyleClass().add("rectangulo-titular");
			stack.getChildren().addAll(rec, lbl);
			contenido.add(stack, i, filas);
			Objetos.add(stack);
		}
		filas++;
	}
	
	public void filasNuevas() {
		for (int i = 0; i < tamaño; i++) {
			StackPane stack= new StackPane();
			TextField tf= new TextField();
			Rectangle rec = new Rectangle(400/tamaño,14);
			tf.setPrefWidth(400/tamaño);
			tf.setPrefHeight(14);
			tf.setMaxHeight(14);
			tf.getStyleClass().add("rectangulo");
			rec.getStyleClass().add("rectangulo");
			stack.getChildren().addAll(rec, tf);
			contenido.add(stack, i, filas);
			Objetos.add(stack);
		}
		filas++;
	}
	
	
	public void cambio (String nombre, String cambio, int linea) {
		int columnaCambio = nombresVariables.indexOf(nombre);
		StackPane stack = (StackPane) contenido.getChildren().get(linea*tamaño+ columnaCambio);
		TextField tf= (TextField) stack.getChildren().get(1);
		tf.setText(cambio);
	}
	
	
	
	
}
