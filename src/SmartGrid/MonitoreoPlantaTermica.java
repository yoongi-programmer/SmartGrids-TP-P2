package SmartGrid;

import java.time.LocalDateTime;

public class MonitoreoPlantaTermica extends RegistroMonitoreo{
	private double cantidadEmisionCO2;
	private double cantidadCombustible;
	
	public MonitoreoPlantaTermica(LocalDateTime fechaHora, double energia, int cantIncidencias, double cantidadEmisionCO2, double cantidadCombustible) {
		super(fechaHora, energia, cantIncidencias);
		
		if(cantidadEmisionCO2<0 || cantidadCombustible<0) {
			throw new RuntimeException ("Falla en el registro. Las entradas no pueden ser negativas.");
		}
		
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
