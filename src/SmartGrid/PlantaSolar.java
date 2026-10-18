package SmartGrid;
import java.time.LocalDateTime;

public class PlantaSolar extends PlantaRenovable {
	private double horasDeRadiacionDiaria;
	private int cantPanelesInstalados;
	private double superficieCubierta;
	
	
	public PlantaSolar(int idPlanta, String nombre, String direccion, double capMax, LocalDateTime fecha,
			String fuentePrimaria, double eficienciaPromedio, double horaDeRadiacionDiaria, 
			int cantPanelesInstalados, double superficieCubierta) {
		
		super(idPlanta, nombre, direccion, capMax, fecha, fuentePrimaria, eficienciaPromedio);
		this.horasDeRadiacionDiaria = horaDeRadiacionDiaria;
		this.cantPanelesInstalados = cantPanelesInstalados;
		this.superficieCubierta = superficieCubierta;
	}
	
	public double getHoraDeRadiacionDiaria() {
		return this.horasDeRadiacionDiaria;
	}
	
	public int getCantPanelesInstalados() {
		return this.cantPanelesInstalados;
	}
	
	public double getSuperficieCubierta() {
		return this.superficieCubierta;
	}
	
	@Override
	protected double calcularCuotaMinima() {
		return (super.getCapMax()*super.getEficienciaPromedio()*horasDeRadiacionDiaria)/24;
	}
}
