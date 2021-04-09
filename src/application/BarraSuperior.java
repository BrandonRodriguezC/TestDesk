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
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logica.GestorDeArchivos;

public class BarraSuperior extends HBox {

	Button nuevoEditorCodigo, guardar, abrir, secuenciar, actualizarTabla, agregarFilas, ejecutarSiguiente, comparar,
			limpiar, ejecucionAutonoma, manual;
	Controlador ctrl;
	Stage presentacionMain;
	ComboBox<String> registroCB;

	public BarraSuperior(Controlador ctrl, Stage main) {
		this.ctrl = ctrl;
		this.presentacionMain = main;

//		Group svg = new Group(
//				crearTrazo("M256 48C141.31 48 48 141.31 48 256s93.31 208 208 208 208-93.31 208-208S370.69 48 256 48zm80 224h-64v64a16 16 0 01-32 0v-64h-64a16 16 0 010-32h64v-64a16 16 0 0132 0v64h64a16 16 0 010 32z", "green",
//						"darkred"),
//				crearTrazo("M256 176v160M336 256H176", "green", "darkblue"));
//
//		Bounds bounds = svg.getBoundsInParent();

//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		nuevoEditorCodigo = new Button();
		nuevoEditorCodigo.setTooltip(new Tooltip("Nuevo editor de codigo"));
		ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/nuevo.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		nuevoEditorCodigo.setGraphic(iv);

//		nuevoEditorCodigo.setGraphic(svg);
//		nuevoEditorCodigo.setMaxSize( 25,  25);
//		nuevoEditorCodigo.setMinSize(25,  25);
//		nuevoEditorCodigo.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

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

//		svg = new Group(
//				crearTrazo("M380.93 57.37A32 32 0 00358.3 48H94.22A46.21 46.21 0 0048 94.22v323.56A46.21 46.21 0 0094.22 464h323.56A46.36 46.36 0 00464 417.78V153.7a32 32 0 00-9.37-22.63zM256 416a64 64 0 1164-64 63.92 63.92 0 01-64 64zm48-224H112a16 16 0 01-16-16v-64a16 16 0 0116-16h192a16 16 0 0116 16v64a16 16 0 01-16 16z", "black","darkred")
//				);
//
//		bounds = svg.getBoundsInParent();
//		scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		guardar = new Button();
		guardar.setTooltip(new Tooltip("Guardar archivo"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/guardar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		guardar.setGraphic(iv);
//		guardar.setGraphic(svg);
//		guardar.setMaxSize(scale + 30, scale + 30);
//		guardar.setMinSize(scale + 30, scale + 30);
//		guardar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

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

//		svg = new Group(
//				crearTrazo("M64 192v-72a40 40 0 0140-40h75.89a40 40 0 0122.19 6.72l27.84 18.56a40 40 0 0022.19 6.72H408a40 40 0 0140 40v40", "rgb(255,204,0)" ,"darkred"),
//				crearTrazo("M479.9 226.55L463.68 392a40 40 0 01-39.93 40H88.25a40 40 0 01-39.93-40L32.1 226.55A32 32 0 0164 192h384.1a32 32 0 0131.8 34.55z", "rgb(255,204,0)" ,"darkred")
//				);
//
//		bounds = svg.getBoundsInParent();
//		scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		abrir = new Button();
		abrir.setTooltip(new Tooltip("Abrir archivo"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/abrir.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		abrir.setGraphic(iv);
//		abrir.setGraphic(svg);
//		abrir.setMaxSize(scale + 30, scale + 30);
//		abrir.setMinSize(scale + 30, scale + 30);
//		abrir.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

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

//		svg = new Group(
//				crearTrazo("M112 111v290c0 17.44 17 28.52 31 20.16l247.9-148.37c12.12-7.25 12.12-26.33 0-33.58L143 90.84c-14-8.36-31 2.72-31 20.16z", "green" ,"darkred")
//				);
//
//		bounds = svg.getBoundsInParent();
//		scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		secuenciar = new Button();
		secuenciar.setTooltip(new Tooltip("Ejecutar"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/ejecutar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		secuenciar.setGraphic(iv);
//		secuenciar.setGraphic(svg);
//		secuenciar.setMaxSize(scale + 30, scale + 30);
//		secuenciar.setMinSize(scale + 30, scale + 30);
//		secuenciar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		secuenciar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.limpiarTablas();
				ctrl.secuenciar();
			}
		});

//		svg = new Group(
//				crearTrazo("M464 256c0-114.87-93.13-208-208-208S48 141.13 48 256s93.13 208 208 208 208-93.13 208-208zm-212.65 91.36a16 16 0 01-.09-22.63L303.58 272H170a16 16 0 010-32h133.58l-52.32-52.73A16 16 0 11274 164.73l79.39 80a16 16 0 010 22.54l-79.39 80a16 16 0 01-22.65.09z", "rgb(0, 0, 128)" ,"darkred")
//				);
//
//		bounds = svg.getBoundsInParent();
//		scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		ejecutarSiguiente = new Button();
		ejecutarSiguiente.setTooltip(new Tooltip("Ejecutar siguiente instruccion"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/siguiente-inhabilitado.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		ejecutarSiguiente.setGraphic(iv);
//		ejecutarSiguiente.setGraphic(svg);
//		ejecutarSiguiente.setMaxSize(scale + 30, scale + 30);
//		ejecutarSiguiente.setMinSize(scale + 30, scale + 30);
//		ejecutarSiguiente.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		ejecutarSiguiente.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.ejecutarSiguienteInstruccion();
			}
		});

//		svg = new Group(
//				crearTrazo("M256 32A224 224 0 0097.61 414.39 224 224 0 10414.39 97.61 222.53 222.53 0 00256 32zM64 256c0-105.87 86.13-192 192-192v384c-105.87 0-192-86.13-192-192z", "black" ,"darkred")
//				);
//
//		bounds = svg.getBoundsInParent();
//		scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		comparar = new Button();
		comparar.setTooltip(new Tooltip("Comparar"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/comparar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		comparar.setGraphic(iv);
//		comparar.setGraphic(svg);
//		comparar.setMaxSize(scale + 30, scale + 30);
//		comparar.setMinSize(scale + 30, scale + 30);
//		comparar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		comparar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.compararTablas();
			}
		});

//		svg = new Group(
//				crearTrazo("M256 48C141.31 48 48 141.32 48 256c0 114.86 93.14 208 208 208 114.69 0 208-93.31 208-208 0-114.87-93.13-208-208-208zm0 313a94 94 0 010-188h4.21l-14.11-14.1a14 14 0 0119.8-19.8l40 40a14 14 0 010 19.8l-40 40a14 14 0 01-19.8-19.8l18-18c-2.38-.1-5.1-.1-8.1-.1a66 66 0 1066 66 14 14 0 0128 0 94.11 94.11 0 01-94 94z", "black" ,"darkred")
//				);
//
//		bounds = svg.getBoundsInParent();
//		scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
//		svg.setScaleX(scale);
//		svg.setScaleY(scale);

		limpiar = new Button();
		limpiar.setTooltip(new Tooltip("Limpiar tablas"));
		iv = new ImageView(new Image(getClass().getResourceAsStream("/assets/limpiar.png")));
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		limpiar.setGraphic(iv);
//		limpiar.setGraphic(svg);
//		limpiar.setMaxSize(scale + 30, scale + 30);
//		limpiar.setMinSize(scale + 30, scale + 30);
//		limpiar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		limpiar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ctrl.limpiarTablas();
			}
		});
		
		Separator separador = new Separator();
		separador.setOrientation(Orientation.VERTICAL);

		

		

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
//		limpiar.setGraphic(svg);
//		limpiar.setMaxSize(scale + 30, scale + 30);
//		limpiar.setMinSize(scale + 30, scale + 30);
//		limpiar.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

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
						if (segundos==0) {
							segundos= 1;
						}
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
		
		getChildren().addAll(nuevoEditorCodigo, guardar, abrir, secuenciar, ejecutarSiguiente, comparar, limpiar,
				separador, ejecucionAutonoma, segundosTF, segundoslbl, separador2, registrolbl, registroCB, separador3,manual);
		
		this.setAlignment(Pos.CENTER);
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPadding(new Insets(5, 5, 5, 5));
		this.setSpacing(2);
	}

	private static SVGPath crearTrazo(String contenido, String relleno, String hoverRelleno) {
		SVGPath path = new SVGPath();
		path.getStyleClass().add("svg");
		path.setContent(contenido);
		path.setStyle("-fx-fill:" + relleno + ";-hover-fill:" + hoverRelleno + ';');
		return path;
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
