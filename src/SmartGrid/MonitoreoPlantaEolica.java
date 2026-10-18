package SmartGrid;

import java.time.LocalDateTime;

public class MonitoreoPlantaEolica extends RegistroMonitoreo {
	private double velocidadVientoPromedio;
	
	public MonitoreoPlantaEolica(LocalDateTime fechaHora, double energia, int cantIncidencias, double velocidadVientoPromedio) {
		super(fechaHora, energia, cantIncidencias);
		
		if(velocidadVientoPromedio<0) {
			throw new RuntimeException ("Falla en el registro. Las entradas no pueden ser negativas.");
		}
		
		this.velocidadVientoPromedio = velocidadVientoPromedio;
	}

	public double getVelocidadVientoPromedio() {
		return velocidadVientoPromedio;
	}
	
}
