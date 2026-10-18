package SmartGrid;
import java.time.LocalDateTime;

public class PlantaTermica extends PlantaNoRenovable{
	
	private double alturaTorres;
	
	public PlantaTermica(int idPlanta, String nombre, String direccion,
            double capMax, LocalDateTime fecha,
            String tipoCombustible, double objetivoEmision,
            double capCombustible, double alturaTorres) {
		super(idPlanta, nombre, direccion, capMax, fecha, tipoCombustible, objetivoEmision, capCombustible);
		this.alturaTorres = alturaTorres;
	}
	
	public double getAlturaTorres() {
		return this.alturaTorres;
	}

	@Override
	protected double calcularCuotaMinima() {
		return super.getCapMax()/2;
	}
	
	@Override
	protected boolean cumplioCuota() {
		if(super.getRegistros().isEmpty()) {
			return false;
		}
		
		double cuotaMinima = consultarCuotaMinima();
		boolean cumplioCuota = true;
		
		for(RegistroMonitoreo registro:super.getRegistros()) {
			MonitoreoPlantaTermica registroPlantaTermica = (MonitoreoPlantaTermica) registro; 
			boolean superoCuotaMinima = registro.getEnergia() >= cuotaMinima;
			boolean noSuperoObjetivoEmision = registroPlantaTermica.getCantidadEmisionCO2() < super.getObjetivoEmision();
			cumplioCuota = cumplioCuota && superoCuotaMinima && noSuperoObjetivoEmision;
		}
		
		return cumplioCuota;
	}
}
