package application;

import java.util.ArrayList;

import controlador.Controlador;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class EditorCodigo extends TabPane {
	ArrayList<CodeArea> areasDeCodigo;
	Controlador ctrl;
	public EditorCodigo(Controlador ctrl) {
		this.ctrl= ctrl;
		CodeArea areaCodigo = new CodeArea(ctrl);
		areasDeCodigo = new ArrayList<CodeArea>();
		Tab first = new Tab("Main.td", areaCodigo);
		areasDeCodigo.add(areaCodigo);
		areaCodigo.getStyleClass().add("margen");
		getTabs().add(first);
		setPrefWidth(400);
	}
	
	public String tomarCodigo() {
		 int indiceCodigoArea =getSelectionModel().getSelectedIndex();
		 CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		 return aux.getCode(aux.getContent());
	}
	
	public int numeroDeLineas() {
		 int indiceCodigoArea =getSelectionModel().getSelectedIndex();
		 CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		 return aux.numeroDeLineas();
	}
	
	public void ponerCodigo(String codigo) {
		 int indiceCodigoArea =getSelectionModel().getSelectedIndex();
		 CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		 aux.getContent().setText(codigo);
		 aux.update();
	}
	
	public int numeroPestañas() {
		return getTabs().size();
	}
	
	public void nuevaPestaña(String nombre) {
		CodeArea areaCodigo = new CodeArea(ctrl);
		areasDeCodigo.add(areaCodigo);
		Tab tab = new Tab(nombre,areaCodigo );
		getTabs().add(tab);
		getSelectionModel().select(tab);
	}
	
	public ArrayList<String> tomarIdentificadores() {
		 int indiceCodigoArea =getSelectionModel().getSelectedIndex();
		 CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		 return aux.getTablaDeSimbolos();
	}
	
	public void señalarLineaEnCodigo(int numeroDeLinea) {
		 int indiceCodigoArea =getSelectionModel().getSelectedIndex();
		 CodeArea aux = areasDeCodigo.get(indiceCodigoArea);
		 aux.señalarLineaEnCodigo(numeroDeLinea);
	}
}
