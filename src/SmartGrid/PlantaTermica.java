package SmartGrid;
import java.time.LocalDate;

public class PlantaTermica extends PlantaNoRenovable{
	
	private double alturaTorres;
	
	public PlantaTermica(int idPlanta, String nombre, String direccion,
            double capMax, LocalDate fecha,
            String tipoCombustible, double objetivoEmision,
            double capCombustible, double alturaTorres) {
		super(idPlanta, nombre, direccion, capMax, fecha, tipoCombustible, objetivoEmision, capCombustible);
		this.alturaTorres = alturaTorres;
	}
	
	public double getAlturaTorres() {
		return this.alturaTorres;
	}

}
