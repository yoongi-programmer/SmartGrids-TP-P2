package SmartGrid;

import java.time.LocalDateTime;

public abstract class PlantaRenovable extends Planta{
	private String fuentePrimaria;
	private double eficienciaPromedio;
	
	public PlantaRenovable(int idPlanta, String nombre, String direccion, double capMax, LocalDateTime fecha, String fuentePrimaria, double eficienciaPromedio) {
		super(idPlanta, nombre, direccion, capMax, fecha);
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

