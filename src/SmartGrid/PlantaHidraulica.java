package SmartGrid;
import java.time.LocalDate;

public class PlantaHidraulica extends PlantaRenovable{
	
	private double capacidadEmbalse;
	private int cantTurbinas;
	
	public PlantaHidraulica(String nombre, String direccion, double capMax, String fecha, String fuentePrimaria,	double eficienciaPromedio,double capacidadEmbalse, int cantTurbinas) {
		super(nombre, direccion, capMax, fecha, fuentePrimaria, eficienciaPromedio);
		this.capacidadEmbalse = capacidadEmbalse;
		this.cantTurbinas = cantTurbinas;
	}
	
	public double getCapacidadEmbalse() {
		return this.capacidadEmbalse;
	}
	
	public int getCantTurbinas() {
		return this.cantTurbinas;
	}
}