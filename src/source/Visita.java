package source;

import java.io.Serializable;
import java.util.Date;

public class Visita implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Comercial comercial;
	private Date fecha;
	private Empresa empresa;

	public Visita() {
	}

	public Visita(Comercial comercial, Date fecha, Empresa empresa) {
		super();
		this.comercial = comercial;
		this.fecha = fecha;
		this.empresa = empresa;
	}

	public Comercial getComercial() {
		return comercial;
	}

	public void setComercial(Comercial comercial) {
		this.comercial = comercial;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return "Visita [comercial=" + comercial + ", fecha=" + fecha + ", empresa=" + empresa + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Visita other = (Visita) obj;
		return other.fecha.equals(this.fecha) && other.comercial.equals(this.comercial)
				&& other.empresa.equals(this.empresa);
	}

}
