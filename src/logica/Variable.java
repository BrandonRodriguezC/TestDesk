package logica;

public class Variable {
	String valor, nombre, tipo;
	
	public Variable(String valor, String tipo, String nombre) {
		if(!valor.isEmpty() && !nombre.isEmpty()) {
			this.valor = valor;
			this.tipo = tipo;
			this.nombre=nombre;
		}else if (!valor.isEmpty() && nombre.isEmpty()) {
			this.valor = valor;
			this.tipo = tipo;
			this.nombre="AUX";
		}else if (valor.isEmpty() && !nombre.isEmpty()) {
			this.tipo = tipo;
			this.nombre=nombre;
			this.valor = (tipo.equals("E")) ? "0":
				    (tipo.equals("R"))? "0.0":
					(tipo.equals("T"))? "": "falso";
		}
	}
	
	public String toString() {
		return this.valor +" "+this.tipo +" " +this.nombre;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		if(valor.charAt(0)=='-' && valor.charAt(1)=='-') {
			this.valor =valor.substring(2);
		}else {
		this.valor = valor;
		}
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
