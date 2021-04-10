package application;

import java.util.ArrayList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class Consolas extends TabPane {

	Tab pestañaConsolaDeErrores, pestañaConsolaDeEntradas, pestañaEstructura;
	TextArea consolaErrores, consolaEstructura;
	ConsolaEntradas areaEntrada;
	int numeroDeLineas;
	boolean desarrollador;

	public Consolas() {
		consolaErrores = new TextArea();
		consolaErrores.setEditable(false);
		pestañaConsolaDeErrores = new Tab("Consola de Errores", consolaErrores);

		areaEntrada = new ConsolaEntradas();
		pestañaConsolaDeEntradas = new Tab("Consola de Entradas", areaEntrada);
		widthProperty().addListener((observable, oldValue, newValue) -> {
			areaEntrada.actualizarTamaño(newValue.doubleValue());

		});

		consolaEstructura = new TextArea();
		consolaEstructura.setEditable(false);
		pestañaEstructura = new Tab("Estructura", consolaEstructura);

		pestañaConsolaDeErrores.setClosable(false);
		pestañaConsolaDeEntradas.setClosable(false);
		pestañaEstructura.setClosable(false);

		getTabs().addAll(pestañaConsolaDeErrores, pestañaConsolaDeEntradas, pestañaEstructura);

		setPrefWidth(400);
		numeroDeLineas = 0;
		desarrollador = false;
	}

	public void presentarErrores(ArrayList<String> errores) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < errores.size(); i++) {
			if (desarrollador) {
				sb.append(errores.get(i)).append('\n');
			} else if (errores.get(i).contains("ERROR L") || errores.get(i).contains("EJECUCIÓN:")) {
				sb.append(errores.get(i)).append('\n');
			}
		}

		consolaErrores.setText(sb.toString());
	}

	public void presentarEstructura(ArrayList<String> estructura) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < estructura.size(); i++) {
				sb.append(estructura.get(i)).append('\n');
		}

		consolaEstructura.setText(sb.toString());
	}

	public void actualizarConsolaEntradas(int numeroDeLineas) {
		if (numeroDeLineas != this.numeroDeLineas) {
			int diferencia = numeroDeLineas - this.numeroDeLineas;
			if (diferencia > 0) {
				for (int i = 0; i < diferencia; i++) {
					areaEntrada.agregarFila();
				}
				/** Agrega Filas en Tabla de Entradas */
			} else {
				diferencia = Math.abs(diferencia);
				for (int i = 0; i < diferencia; i++) {
					areaEntrada.eliminarFila();
				}
				/** Elimina filas en Tabla de Entradas */
			}
			this.numeroDeLineas = numeroDeLineas;
		}
	}

	public void escribirEnConsola(String expresion, int numeroDeLinea) {
		areaEntrada.escribir(expresion, numeroDeLinea);
	}

	public String leerLinea(int numeroDeLinea) {
		return areaEntrada.leerLinea(numeroDeLinea);
	}

	public void limpiar() {
		areaEntrada.limpiar();
	}

	public boolean isDesarrollador() {
		return desarrollador;
	}

	public void setDesarrollador(boolean desarrollador) {
		this.desarrollador = desarrollador;
	}

}
