package SmartGrid;

import java.time.LocalDateTime;

public abstract class PlantaRenovable extends Planta{
	private String fuentePrimaria;
	private double eficienciaPromedio;
	
	public PlantaRenovable(String nombre, String direccion, double capMax, LocalDateTime fecha, String fuentePrimaria, double eficienciaPromedio) {
		super(nombre, direccion, capMax, fecha);
		
		if(eficienciaPromedio < 0) {
			throw new RuntimeException("Eficiencia promedio debe ser >= 0");
		}
		if(fuentePrimaria == null) {
			throw new RuntimeException("Debe ingresar una fuente primaria válida");
		}
		this.fuentePrimaria = fuentePrimaria;
		this.eficienciaPromedio = eficienciaPromedio;
	}

	public String getFuentePrimaria() {
		return fuentePrimaria;
	}

	public double getEficienciaPromedio() {
		return eficienciaPromedio;
	}
	
	@Override
	public String toString() {
	    return super.toString() + " | Fuente: " + fuentePrimaria + " | Eficiencia: " + eficienciaPromedio;
	}
}

