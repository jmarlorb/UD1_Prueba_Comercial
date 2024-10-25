package source;

import java.io.Serializable;

public class Comercial implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;

	public Comercial() {
	}

	public Comercial(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comercial other = (Comercial) obj;
		return nombre.equals(other.nombre);
	}

	@Override
	public String toString() {
		return "Comercial [nombre=" + nombre + "]";
	}

}
