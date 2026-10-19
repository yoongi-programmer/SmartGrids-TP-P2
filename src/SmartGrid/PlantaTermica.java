package SmartGrid;
import java.time.LocalDate;

public class PlantaTermica extends PlantaNoRenovable{
	private double objetivoEmision;
	private double capCombustible;
	
	public PlantaTermica(String nombre, String direccion, double capMax, String fecha, String tipoCombustible, double objetivoEmision, double capCombustible) {
		super(nombre, direccion, capMax, fecha, tipoCombustible);
		this.objetivoEmision = objetivoEmision;
		this.capCombustible = capCombustible;
	}
	
	public double getObjetivoEmision() {
		return objetivoEmision;
	}

	public double getCapCombustible() {
		return capCombustible;
	}

}
