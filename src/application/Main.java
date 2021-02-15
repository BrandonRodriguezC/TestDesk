package application;

import controlador.Controlador;
import javafx.application.Application;
import javafx.stage.Stage;
import logica.GestorDeArchivos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @author brandonrodriguez TODO: - Doble declaracion 
 * 			- Guardar y Cargar Archivos
 * 			- Secuenciador 
 * 			- Tabla de simbolos 
 * 			- Ejecutor 
 * 			- Manipulador de Errores 
 * 			- Presentacion (lecto-escritura IPO) 
 * 			- Prueba de escritorio
 */

public class Main extends Application  implements GestorDeArchivos{
	Scene scene;
	Tabla tabla;
	
	BarraSuperior bs;
	EditorCodigo ec;
	Controlador ctrl;
	Consolas cs;
	Tablas ts;

	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = new BorderPane();
			scene = new Scene(root, 1200, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			ctrl= new Controlador();
			
			bs= new BarraSuperior(ctrl, primaryStage);
			
			ts = new Tablas();
	        ctrl.setTS(ts);
	        
	        cs = new Consolas();
	        ctrl.setCS(cs);
	        
			ec= new EditorCodigo(ctrl);
			ctrl.setEC(ec);
			
			HBox areas= new HBox();
			areas.getChildren().addAll(ec,ts, cs);
			
			root.setCenter(areas);
			root.setTop(new HBox(bs));
			
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
