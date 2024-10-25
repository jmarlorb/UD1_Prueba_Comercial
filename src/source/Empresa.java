package source;

import java.io.Serializable;

public class Empresa implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cif;
	private String nombre;

	public Empresa() {
	}

	public Empresa(int cif, String nombre) {
		super();
		this.cif = cif;
		this.nombre = nombre;
	}

	public Empresa(char[] cif, String nombre) {
		super();
		this.nombre = nombre;
	}

	public int getCif() {
		return cif;
	}

	public void setCif(int cif) {
		this.cif = cif;
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
		Empresa other = (Empresa) obj;
		return other.nombre.equals(this.nombre);
	}

	@Override
	public String toString() {
		return "Empresa [cif=" + cif + ", nombre=" + nombre + "]";
	}

}
