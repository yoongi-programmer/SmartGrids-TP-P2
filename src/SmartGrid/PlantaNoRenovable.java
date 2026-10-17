package SmartGrid;

import java.time.LocalDate;

public abstract class PlantaNoRenovable extends Planta {
	private String tipoCombustible;
	private double objetivoEmision;
	private double capCombustible;
	
	public PlantaNoRenovable(int idPlanta, String nombre, String direccion, double capMax, LocalDate fecha, String tipoCombustible, double objetivoEmision, double capCombustible) {
		super(idPlanta, nombre, direccion, capMax, fecha);
		this.tipoCombustible = tipoCombustible;
		this.objetivoEmision = objetivoEmision;
		this.capCombustible = capCombustible;
	}

	public String getTipoCombustible() {
		return tipoCombustible;
	}

	public double getObjetivoEmision() {
		return objetivoEmision;
	}

	public double getCapCombustible() {
		return capCombustible;
	}
	
}
