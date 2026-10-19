package SmartGrid;

import java.time.LocalDate;

public abstract class PlantaRenovable extends Planta{
	private String fuentePrimaria;
	private double eficienciaPromedio;
	
	public PlantaRenovable(String nombre, String direccion, double capMax, String fecha, String fuentePrimaria, double eficienciaPromedio) {
		super(nombre, direccion, capMax, fecha);
		this.fuentePrimaria = fuentePrimaria;
		this.eficienciaPromedio = eficienciaPromedio;
	}

	public String getFuentePrimaria() {
		return fuentePrimaria;
	}

	public double getEficienciaPromedio() {
		return eficienciaPromedio;
	}
}

