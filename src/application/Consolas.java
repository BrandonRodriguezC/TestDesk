package application;

import java.util.ArrayList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
public class Consolas extends TabPane {

	Tab pestañaConsolaDeErrores, pestañaConsolaDeEntradas;
	TextArea  consolaErrores;
	ConsolaEntradas areaEntrada;
	int numeroDeLineas;
	
	public Consolas() {
		consolaErrores = new TextArea();
		consolaErrores.setEditable(false);
		pestañaConsolaDeErrores = new Tab("Consola de Errores", consolaErrores);
		
		areaEntrada = new ConsolaEntradas();
		pestañaConsolaDeEntradas = new Tab("Consola de Entradas", areaEntrada);
		widthProperty().addListener((observable, oldValue, newValue) ->
        {
        	areaEntrada.actualizarTamaño(newValue.doubleValue());
        	
        });
		pestañaConsolaDeErrores.setClosable(false);
		pestañaConsolaDeEntradas.setClosable(false);
		
		getTabs().addAll(pestañaConsolaDeErrores, pestañaConsolaDeEntradas);
	
		setPrefWidth(400);
		numeroDeLineas=0;
	}
	
	public void presentarErrores(ArrayList<String> errores){
		String texto = String.join( "\n", errores);
		consolaErrores.setText(texto);
	}

	
	public void actualizarConsolaEntradas(int numeroDeLineas) {
		if (numeroDeLineas!=this.numeroDeLineas) {
			int diferencia = numeroDeLineas - this.numeroDeLineas;
			if(diferencia > 0) {
				for (int i = 0; i < diferencia; i++) {
					areaEntrada.agregarFila();
				}
				/** Agrega Filas en Tabla de Entradas */
			}else {
				diferencia= Math.abs(diferencia);
				for (int i = 0; i < diferencia; i++) {
					areaEntrada.eliminarFila();
				}
				/** Elimina filas en Tabla de Entradas */
			}
			this.numeroDeLineas=numeroDeLineas;
		}
	}
	
	public void escribirEnConsola(String expresion, int numeroDeLinea) {
		areaEntrada.escribir(expresion, numeroDeLinea);
	}
	
	public String leerLinea(int numeroDeLinea){
		return areaEntrada.leerLinea(numeroDeLinea);
	}
	
	public void limpiar(){
		areaEntrada.limpiar();
	}

}
