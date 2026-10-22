package SmartGrid;
import java.time.LocalDateTime;

public class PlantaSolar extends PlantaRenovable {
	private double horasDeRadiacionDiaria;
	private int cantPanelesInstalados;
	private double superficieCubierta;
	
	
	public PlantaSolar(String nombre, String direccion, double capMax, LocalDateTime fecha,String fuentePrimaria, double eficienciaPromedio, double horaDeRadiacionDiaria, 
			int cantPanelesInstalados, double superficieCubierta) {
		super(nombre, direccion, capMax, fecha, fuentePrimaria, eficienciaPromedio);
		
		if(horasDeRadiacionDiaria < 0 || horasDeRadiacionDiaria > 24) {
			throw new RuntimeException("Debe ingresar una valor numérico válido");
		}
		if(cantPanelesInstalados < 0) {
			throw new RuntimeException("Debe ingresar una cantidad de paneles válida");
		}
		if(superficieCubierta < 0) {
			throw new RuntimeException("Debe ingresar una superficie cubierta válida");
		}
		this.horasDeRadiacionDiaria = horaDeRadiacionDiaria;
		this.cantPanelesInstalados = cantPanelesInstalados;
		this.superficieCubierta = superficieCubierta;
	}
	
	public double getHorasDeRadiacionDiaria() {
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
	
	@Override
	public String toString() {
	    return super.toString() + " | Paneles: " + cantPanelesInstalados + " | RadDiaria: " + horasDeRadiacionDiaria;
	}
}
