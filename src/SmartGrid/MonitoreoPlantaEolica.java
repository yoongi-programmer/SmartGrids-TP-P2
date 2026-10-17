package SmartGrid;

import java.time.LocalDateTime;

public class MonitoreoPlantaEolica extends RegistroMonitoreo {
	private double velocidadVientoPromedio;
	
	public MonitoreoPlantaEolica(int idRegistro, LocalDateTime fechaHora, double energia, int cantIncidencias, double velocidadVientoPromedio) {
		super(idRegistro, fechaHora, energia, cantIncidencias);
		this.velocidadVientoPromedio = velocidadVientoPromedio;
	}

	public double getVelocidadVientoPromedio() {
		return velocidadVientoPromedio;
	}
	
}
