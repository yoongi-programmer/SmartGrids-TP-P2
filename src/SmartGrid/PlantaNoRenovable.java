package SmartGrid;

import java.time.LocalDate;

public abstract class PlantaNoRenovable extends Planta {
	private String tipoCombustible;
	
	public PlantaNoRenovable(String nombre, String direccion, double capMax, String fecha, String tipoCombustible) {
		super(nombre, direccion, capMax, fecha);
		this.tipoCombustible = tipoCombustible;
	}

	public String getTipoCombustible() {
		return tipoCombustible;
	}
	
}
