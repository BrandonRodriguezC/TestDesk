package application;

import org.fxmisc.richtext.InlineCssTextArea;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Consolas extends TabPane {

	Tab pestañaConsolaDeErrores, pestañaConsolaDeEntradas;
	Button btn;
	HBox entrada;
	VBox areaEntrada;
	TextArea consolaEntradas, consolaErrores;
	//InlineCssTextArea consolaErrores;
	public Consolas() {
		//consolaErrores= new InlineCssTextArea();
		consolaErrores = new TextArea();
		consolaEntradas = new TextArea();
		consolaEntradas.setPrefHeight(550);
		TextField tf = new TextField();
		tf.setPrefWidth(300);
		btn = new Button("Ingresar");
		btn.setPrefWidth(100);
		entrada = new HBox(tf, btn);
		areaEntrada = new VBox(consolaEntradas, entrada);
		pestañaConsolaDeErrores = new Tab("Consola de Errores", consolaErrores);
		pestañaConsolaDeEntradas = new Tab("Consola de Entradas", areaEntrada);
		getTabs().add(pestañaConsolaDeErrores);
		getTabs().add(pestañaConsolaDeEntradas);
		consolaErrores.setEditable(false);
		setPrefWidth(400);
	}
	
	public void presentarErrores(String errores){
//		System.out.println("presentando");
		/*int desde = consolaErrores.getText().length();
		int hasta = desde+ errores.length();
		consolaErrores.appendText(errores);
		consolaErrores.setStyle(desde, hasta, "-fx-fill: red;");*/
		consolaErrores.setText(errores);
	}

//	public TextArea getConsolaErrores() {
//		return consolaErrores;
//	}

//	public void setConsolaErrores(TextArea consolaErrores) {
//		this.consolaErrores = consolaErrores;
//	}

	public TextArea getConsolaEntradas() {
		return consolaEntradas;
	}

	public void setConsolaEntradas(TextArea consolaEntradas) {
		this.consolaEntradas = consolaEntradas;
	}

}
