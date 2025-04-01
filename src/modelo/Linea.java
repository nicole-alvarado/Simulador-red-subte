package modelo;

import java.util.Objects;

public class Linea {

	private RedSubte red;
	private String codigo;
	private String nombre;
	
	public Linea(RedSubte red , String codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.red = red;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}
	
	public RedSubte getRed() {
		return red;
	}

	public void setRed(RedSubte red) {
		this.red = red;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(codigo, red);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linea other = (Linea) obj;
		return Objects.equals(codigo, other.codigo) && Objects.equals(red, other.red);
	}

	@Override
	public String toString() {
		return "Linea [codigo=" + codigo + ", nombre=" + nombre + "]";
	}

	
	
	
}
