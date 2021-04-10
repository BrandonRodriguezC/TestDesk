package application;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import controlador.Controlador;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logica.GestorDeArchivos;

public class BarraSuperior extends HBox {

	Button nuevoEditorCodigo, guardar, abrir, secuenciar, actualizarTabla, agregarFilas, ejecutarSiguiente, comparar,
			limpiar, ejecucionAutonoma, manual, desarrollador;
	Controlador ctrl;
	Stage presentacionMain;
	ComboBox<String> registroCB;

	public BarraSuperior(Controlador ctrl, Stage main) {
		this.ctrl = ctrl;
		this.presentacionMain = main;

		Separator separador = new Separator();
		separador.setOrientation(Orientation.VERTICAL);
		
		nuevoEditorCodigo = new Button();
		nuevoEditorCodigo.setTooltip(new Tooltip("Nuevo editor de codigo"));
		ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/nuevo.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		nuevoEditorCodigo.setGraphic(iv);
		nuevoEditorCodigo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setHeaderText("Creacion de archivo");
				dialog.setContentText("Ingresa el nombre del nuevo archivo:");
				Optional<String> result = dialog.showAndWait();
				String nombre = result.isPresent() && !result.get().equals("") ? result.get() + ".td"
						: "main " + (ctrl.numeroPestañasEC() + 1) + ".td";
				if (result.isPresent()) {
					ctrl.añadirNuevaPestañaEC(nombre);
				}
			}
		});

		guardar = new Button();
		guardar.setTooltip(new Tooltip("Guardar archivo"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/guardar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		guardar.setGraphic(iv);
		guardar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File ruta = fileChooser.showSaveDialog(presentacionMain);
				if (ruta != null) {
					try {
						GestorDeArchivos.EscribirArchivo(ctrl.tomarCodigo(), ruta);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});


		abrir = new Button();
		abrir.setTooltip(new Tooltip("Abrir archivo"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/abrir.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		abrir.setGraphic(iv);
		abrir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File ruta = fileChooser.showOpenDialog(presentacionMain);
				if (ruta != null) {
					try {
						ctrl.ponerCodigo(GestorDeArchivos.AbrirArchivo(ruta));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});


		secuenciar = new Button();
		secuenciar.setTooltip(new Tooltip("Ejecutar"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/ejecutar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		secuenciar.setGraphic(iv);
		secuenciar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.limpiarTablas();
				ctrl.secuenciar();
			}
		});


		ejecutarSiguiente = new Button();
		ejecutarSiguiente.setTooltip(new Tooltip("Ejecutar siguiente instruccion"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/siguiente-inhabilitado.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		ejecutarSiguiente.setGraphic(iv);
		ejecutarSiguiente.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.ejecutarSiguienteInstruccion();
			}
		});

		comparar = new Button();
		comparar.setTooltip(new Tooltip("Comparar"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/comparar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		comparar.setGraphic(iv);
		comparar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.compararTablas();
			}
		});

		limpiar = new Button();
		limpiar.setTooltip(new Tooltip("Limpiar tablas"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/limpiar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		limpiar.setGraphic(iv);
		limpiar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.limpiarTablas();
			}
		});
		
		Separator separador1 = new Separator();
		separador1.setOrientation(Orientation.VERTICAL);

		

		

		Label segundoslbl = new Label("segundos");

		TextField segundosTF = new TextField();
		segundosTF.setId("segundos");
		segundosTF.setPrefWidth(50);
		segundosTF.setPadding(new Insets(0, 2, 0, 2));

		ejecucionAutonoma = new Button();
		ejecucionAutonoma.setTooltip(new Tooltip("Ejecutar automaticamente"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/ejecucion-automatica.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		ejecucionAutonoma.setGraphic(iv);
		ejecucionAutonoma.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// iniciar hilo
				int segundos= 1;
				try {
					String segundosString= segundosTF.getText();
					if (!segundosString.isEmpty()) {
						segundos = Integer.parseInt(segundosString); 
						if (segundos<0) {
							ctrl.alertar("Error", "Error", "El campo de segundos debe contener un valor entero positivo");
						}
						
					}else {
							segundos= -1;
					}
				} catch (Exception e) {
					ctrl.alertar("Error", "Error", "El campo de segundos debe contener un valor entero positivo");
				}
				ctrl.ejecutarAutomaticamente(segundos);
				
			}
		});
		
		Separator separador2 = new Separator();
		separador2.setOrientation(Orientation.VERTICAL);

		Label registrolbl = new Label("Registro");
		
		registroCB = new ComboBox<>();
		registroCB.setId("registro");
		
		Separator separador3 = new Separator();
		separador3.setOrientation(Orientation.VERTICAL);
		
		manual = new Button();
		manual.setTooltip(new Tooltip("Manual"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/manual.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		manual.setGraphic(iv);
		manual.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// mostrar manual
			}
		});
		
		desarrollador = new Button();
		desarrollador.setTooltip(new Tooltip("Desarrollador"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/desarrollador.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		desarrollador.setGraphic(iv);
		desarrollador.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog();
		        dialog.setTitle("Desarrollador");
		        dialog.setHeaderText("Desarrollador");
		        dialog.setContentText("Inserte la clave:");
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					if (result.get().equals("Brandon")) {
						ctrl.setDesarrolladorConsolas();
					}else if(result.get().equals("espacios")) {
//						ctrl.setDesarrolladorEditorCodigo();
					}
				}
			}
		});
		
		getChildren().addAll(separador, nuevoEditorCodigo, guardar, abrir, secuenciar, ejecutarSiguiente, comparar, limpiar,
				separador1, ejecucionAutonoma, segundosTF, segundoslbl, separador2,desarrollador,separador3
//				, registrolbl, registroCB, separador3,manual
				);
		
		this.setAlignment(Pos.CENTER);
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPadding(new Insets(5, 5, 5, 5));
		this.setSpacing(2);
	}

	public void cambiarIconoEjecucion(boolean valor) {
		ImageView iv;
		if (valor) {
			iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/parar.png")));
			iv.setFitHeight(20);
			iv.setFitWidth(20);
			secuenciar.setGraphic(iv);
			iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/siguiente.png")));
			iv.setFitHeight(20);
			iv.setFitWidth(20);
			ejecutarSiguiente.setGraphic(iv);
		} else {
			iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/ejecutar.png")));
			iv.setFitHeight(20);
			iv.setFitWidth(20);
			secuenciar.setGraphic(iv);
			iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/siguiente-inhabilitado.png")));
			iv.setFitHeight(20);
			iv.setFitWidth(20);
			ejecutarSiguiente.setGraphic(iv);
		}
	}

}
