package SmartGrid;
import java.time.LocalDateTime;

public class PlantaTermica extends PlantaNoRenovable{
	private double objetivoEmision;
	private double capCombustible;
	
	public PlantaTermica(String nombre, String direccion, double capMax, LocalDateTime fecha, String tipoCombustible, double objetivoEmision, double capCombustible) {
		super(nombre, direccion, capMax, fecha, tipoCombustible);
		if(objetivoEmision < 0) {
			throw new RuntimeException("Debe ingresar una valor numérico válido");
		}
		if(capCombustible < 0) {
			throw new RuntimeException("Debe ingresar un valor válido");
		}
		this.objetivoEmision = objetivoEmision;
		this.capCombustible = capCombustible;
	}
	
	public double getObjetivoEmision() {
		return objetivoEmision;
	}

	public double getCapCombustible() {
		return capCombustible;
	}

	@Override
	protected double calcularCuotaMinima() {
		return super.getCapMax()/2;
	}
	
	@Override
	protected boolean cumplioCuotaEnRegistro(RegistroMonitoreo reg) {
	    MonitoreoPlantaTermica registroPlantaTermica = (MonitoreoPlantaTermica) reg;
	    boolean superoCuotaMinima = reg.getEnergia() >= consultarCuotaMinima();
	    boolean noSuperoObjetivoEmision = registroPlantaTermica.getCantidadEmisionCO2() < objetivoEmision;
	    return superoCuotaMinima && noSuperoObjetivoEmision;
	}
	
	@Override
	public String toString() {
	    return super.toString() + " | Objetivo de emision: " + objetivoEmision + " | Capacidad combustible: " + capCombustible;
	}
}
