package modelo;

import java.util.Objects;

public class Tramo {
	
	private Estacion estacionA;
	private Estacion estacionB;
	private int duracion;
	private int tipo;
	private boolean esCombinacion;
	
	public Tramo(Estacion estacion1, Estacion estacion2, int duracion, int tipo, boolean esCombinacion) {
		this.estacionA = estacion1;
		this.estacionB = estacion2;
		this.duracion = duracion;
		this.tipo = tipo;
		this.esCombinacion = esCombinacion;
	}

	public Estacion getEstacion1() {
		return estacionA;
	}

	public void setEstacion1(Estacion estacion1) {
		this.estacionA = estacion1;
	}

	public Estacion getEstacion2() {
		return estacionB;
	}

	public void setEstacion2(Estacion estacion2) {
		this.estacionB = estacion2;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	
	public boolean getEsCombinacion() {
		return esCombinacion;
	}

	public void setEsCombinacion(boolean esCombinacion) {
		this.esCombinacion = esCombinacion;
	}

	@Override
	public String toString() {
		return "Distancia [estacion1=" + estacionA + ", estacion2=" + estacionB + ", duracion=" + duracion + ", tipo="
				+ tipo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(estacionA, estacionB);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tramo other = (Tramo) obj;
		return Objects.equals(estacionA, other.estacionA) && Objects.equals(estacionB, other.estacionB);
	}

	
}
