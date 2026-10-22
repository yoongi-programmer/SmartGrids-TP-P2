package SmartGrid;

import java.time.LocalDateTime;

public abstract class PlantaNoRenovable extends Planta {
	private String tipoCombustible;
	
	public PlantaNoRenovable(String nombre, String direccion, double capMax, LocalDateTime fecha, String tipoCombustible) {
		super(nombre, direccion, capMax, fecha);
		if(tipoCombustible == null || tipoCombustible.length() == 0 || tipoCombustible.length() > 50) {
			throw new RuntimeException("Debe ingresar una tipo de combustible válido");
		}
		this.tipoCombustible = tipoCombustible;
	}

	public String getTipoCombustible() {
		return tipoCombustible;
	}
	
	@Override
	public String toString() {
	    return super.toString() + " | Tipo de Combustible: " + tipoCombustible;
	}
	
}
