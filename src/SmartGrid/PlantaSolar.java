package SmartGrid;
import java.time.LocalDate;

public class PlantaSolar extends PlantaRenovable {
	private double horaDeRadiacionDiaria;
	private int cantPanelesInstalados;
	private double superficieCubierta;
	
	
	public PlantaSolar(String nombre, String direccion, double capMax, String fecha, String fuentePrimaria, double eficienciaPromedio, double horaDeRadiacionDiaria, 
			int cantPanelesInstalados, double superficieCubierta) {
		
		super(nombre, direccion, capMax, fecha, fuentePrimaria, eficienciaPromedio);
		this.horaDeRadiacionDiaria = horaDeRadiacionDiaria;
		this.cantPanelesInstalados = cantPanelesInstalados;
		this.superficieCubierta = superficieCubierta;
	}
	
	public double getHoraDeRadiacionDiaria() {
		return this.horaDeRadiacionDiaria;
	}
	
	public int getCantPanelesInstalados() {
		return this.cantPanelesInstalados;
	}
	
	public double getSuperficieCubierta() {
		return this.superficieCubierta;
	}
}
