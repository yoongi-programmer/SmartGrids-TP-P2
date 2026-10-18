package SmartGrid;
import java.time.LocalDateTime;

public class PlantaEolica extends PlantaRenovable {
	
	private double alturaTorres;
	private double velocidadMinDeViento;
	private int cantAerogeneradores;
	
	public PlantaEolica(int idPlanta, String nombre, String direccion, 
            double capMax, LocalDateTime fecha, 
            String fuentePrimaria, double eficienciaPromedio,
            double alturaTorres, double velocidadMinDeViento, 
            int cantAerogeneradores) {
		super (idPlanta, nombre, direccion, capMax, fecha, fuentePrimaria, eficienciaPromedio);
		this.alturaTorres = alturaTorres;
		this.velocidadMinDeViento = velocidadMinDeViento;
		this.cantAerogeneradores = cantAerogeneradores;
	}
	
	public double getAlturaTorres() {
		return this.alturaTorres;
	}
	
	public double getVelocidadMinDeViento() {
		return this.velocidadMinDeViento;
	}
	
	public int getCantAerogeneradores() {
		return this.cantAerogeneradores;
	}
	
	@Override
	protected double calcularCuotaMinima() {
		return super.getCapMax() * super.getEficienciaPromedio();
	}

}
