package application;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class ConsolaEntradas extends ScrollPane{
	ArrayList<StackPane> Objetos;
	GridPane contenido;
	int  filas, ancho= 200, objetos;
	
	public ConsolaEntradas() {
		filas=0;
		objetos=0;
		Objetos = new ArrayList<>();
		contenido= new GridPane();
		setContent(contenido);
		columnasNuevas();
	}
	
	public void columnasNuevas() {
		getChildren().removeAll(Objetos);
		Objetos = new ArrayList<>();
		filas=0;
		contenido.getChildren().clear();
		
		StackPane stack= new StackPane();
		Label lbl= new Label("Escritura");
		lbl.getStyleClass().add("label-titular-entrada");
		Rectangle rec = new Rectangle(ancho,16);
		rec.getStyleClass().add("rectangulo-titular-entrada");
		stack.getChildren().addAll(rec, lbl);
		contenido.add(stack, 0, filas);
		Objetos.add(stack);
		
		
		stack= new StackPane();
		lbl= new Label("Lectura");
		lbl.getStyleClass().add("label-titular-entrada");
		rec = new Rectangle(ancho,16);
		rec.getStyleClass().add("rectangulo-titular-entrada");
		stack.getChildren().addAll(rec, lbl);
		contenido.add(stack, 1, filas);
		Objetos.add(stack);
		objetos+=2;
		filas++;
	}
	
	public void agregarFila() {
		StackPane stack= new StackPane();
		TextField tf= new TextField();
		Rectangle rec = new Rectangle(ancho,16);
		tf.setEditable(false);
		tf.setPrefWidth(ancho);
		tf.setPrefHeight(14);
		tf.setMaxHeight(14);
		tf.setAlignment(Pos.CENTER);
		tf.getStyleClass().add("rectangulo");
		rec.getStyleClass().add("rectangulo");
		stack.getChildren().addAll(rec, tf);
		contenido.add(stack, 0, filas);
		
		stack= new StackPane();
		tf= new TextField();
		rec = new Rectangle(ancho,16);
		tf.setPrefWidth(ancho);
		tf.setPrefHeight(14);
		tf.setMaxHeight(14);
		tf.setAlignment(Pos.CENTER);
		tf.getStyleClass().add("rectangulo");
		rec.getStyleClass().add("rectangulo");
		stack.getChildren().addAll(rec, tf);
		contenido.add(stack, 1, filas);
		
		objetos+=2;
		filas++;
	}
	
	public void eliminarFila() {
		contenido.getChildren().remove(objetos-2, objetos);
		objetos-=2;
	}
		
	public void cambio(String columna, String cambio, int linea) {
		int columnaCambio =0;
		if(!columna.equals("Escritura")) {
			columnaCambio =1;
		}
		StackPane stack = (StackPane) contenido.getChildren().get(linea*2+ columnaCambio);
		TextField tf= (TextField) stack.getChildren().get(1);
		tf.setText(cambio);
	}
	
	public void escribir(String expresion, int numeroDeLinea) {
		StackPane stack = (StackPane) contenido.getChildren().get(numeroDeLinea*2);
		TextField tf= (TextField) stack.getChildren().get(1);
		tf.setText(expresion);
	}	
	
	public String leerLinea(int numeroDeLinea) {
		StackPane stack = (StackPane) contenido.getChildren().get(numeroDeLinea*2+1);
		TextField tf= (TextField) stack.getChildren().get(1);
		String a = tf.getText();
		return a;
	}
	
	public void limpiar() {
		ObservableList<Node> lista= contenido.getChildren();
		int tamaño= lista.size();
		for (int i = 2; i < tamaño; i++) {
			StackPane stack = (StackPane) lista.get(i);
			TextField tf= (TextField) stack.getChildren().get(1);
			tf.setText("");
		}
	}
	
	public void actualizarTamaño(double tamañoTab) {
		
		ancho=120;
		
		if( tamañoTab/2 > 120 ) {
			ancho= (int) tamañoTab/2 ; 
		}
		
		StackPane  sp = (StackPane) contenido.getChildren().get(0);
		((Rectangle)sp.getChildren().get(0)).setWidth(ancho);
		sp = (StackPane) contenido.getChildren().get(1);
		((Rectangle)sp.getChildren().get(0)).setWidth(ancho);
		
		for (int i = 2; i < objetos; i++) {
			sp = (StackPane) contenido.getChildren().get(i);
			((Rectangle) sp.getChildren().get(0)).setWidth(ancho);
			((TextField) sp.getChildren().get(1)).setMaxWidth(ancho);
			((TextField) sp.getChildren().get(1)).setPrefWidth(ancho);
			((TextField) sp.getChildren().get(1)).setMinWidth(ancho);
		}
	}
	
	
}