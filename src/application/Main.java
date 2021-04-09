package application;

import controlador.Controlador;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import logica.GestorDeArchivos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

/**
 * 
 * @author brandonrodriguez 
 * 
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
			ctrl.setBs(bs);
			ts = new Tablas();
	        ctrl.setTS(ts);
	        
	        cs = new Consolas();
	        ctrl.setCS(cs);
	        
			ec= new EditorCodigo(ctrl);
			ctrl.setEC(ec);
			ctrl.actualizarTablas();
			
			//HBox areas= new HBox();
			GridPane areas = new GridPane();
			
			RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.);
            rc.setValignment(VPos.BOTTOM);
            areas.getRowConstraints().add(rc);
			
	        for (int i = 0 ; i < 3 ; i++) {
	        	ColumnConstraints cc = new ColumnConstraints();
		        cc.setHalignment(HPos.CENTER);
		        cc.setPercentWidth(33.333);
		        areas.getColumnConstraints().add(cc);
	        }
	        
	        areas.add(ec, 0, 0);
	        areas.add(ts, 1, 0);
	        areas.add(cs, 2, 0);
	       
			root.setCenter(areas);
			root.setTop(new HBox(bs));
			primaryStage.setTitle("TestDesk");
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
