package SmartGrid;

import java.time.LocalDateTime;

public class MonitoreoPlantaTermica extends RegistroMonitoreo{
	private double cantidadEmisionCO2;
	private double cantidadCombustible;
	
	public MonitoreoPlantaTermica(int idRegistro, LocalDateTime fechaHora, double energia, int cantIncidencias, double cantidadEmisionCO2, double cantidadCombustible) {
		super(idRegistro, fechaHora, energia, cantIncidencias);
		this.cantidadEmisionCO2 = cantidadEmisionCO2;
		this.cantidadCombustible = cantidadCombustible;
	}

	public double getCantidadEmisionCO2() {
		return cantidadEmisionCO2;
	}

	public double getCantidadCombustible() {
		return cantidadCombustible;
	}
}
