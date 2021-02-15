package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import controlador.Controlador;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logica.GestorDeArchivos;

public class BarraSuperior extends HBox {

	Button nuevoEditorCodigo, guardar, abrir, secuenciar, actualizarTabla, agregarFilas, ejecutarSiguiente;
	Controlador ctrl;
	Stage presentacionMain;
	
	
	public BarraSuperior(Controlador ctrl, Stage main) {
		this.ctrl= ctrl;
		this.presentacionMain= main;
		
		nuevoEditorCodigo = new Button("Nueva pestaña");
		guardar = new Button("Guardar Archivo");
		abrir = new Button("Abrir Archivo");
		secuenciar = new Button("Secuenciar");
		ejecutarSiguiente = new Button("Ejecutar siguiente");
		actualizarTabla = new Button("Actualizar Tabla");
//		agregarFilas = new Button("Agregar Filas a la Tabla");

		nuevoEditorCodigo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setHeaderText("Creacion de archivo");
				dialog.setContentText("Ingresa el nombre del nuevo archivo:");
				Optional<String> result = dialog.showAndWait();
				String nombre = result.isPresent() && !result.get().equals("") ? result.get() + ".td"
						: "Main " + (ctrl.numeroPestañasEC() + 1) + ".td";
				if (result.isPresent()) {
					ctrl.añadirNuevaPestañaEC(nombre);
				}
			}
		});
		
		guardar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File ruta = fileChooser.showSaveDialog(presentacionMain);
				if (ruta != null) {
					try {
						GestorDeArchivos.EscribirArchivo(ctrl.tomarCodigo(), ruta);
						//GestorDeArchivos.EscribirArchivo(firstCA.getCode(firstCA.getContent()), file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		abrir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File ruta = fileChooser.showOpenDialog(presentacionMain);
				if (ruta != null) {
					try {
						String codigo = GestorDeArchivos.AbrirArchivo(ruta);
						ctrl.ponerCodigo(codigo);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		secuenciar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.secuenciar(ctrl.tomarCodigo().replace("\t", "").split("\n"),ctrl.numeroDeLineas());
			}
		});
		
		ejecutarSiguiente.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//ctrl.secuenciar(ctrl.tomarCodigo().replace("\t", "").split("\n"),ctrl.numeroDeLineas());
				ctrl.ejecutarSiguienteInstruccion();
			}
		});

		actualizarTabla.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.actualizarColumnasTablas(ctrl.tomarIdentificadoresActuales(),ctrl.numeroDeLineas());
			}
		});

//		agregarFilas.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				System.out.println("Inhabilitado");
//			}
//		});
		
		getChildren().addAll(nuevoEditorCodigo, guardar, abrir, secuenciar, ejecutarSiguiente, actualizarTabla);
	}
}
