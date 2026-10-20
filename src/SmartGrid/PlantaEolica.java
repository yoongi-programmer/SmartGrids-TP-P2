package SmartGrid;
import java.time.LocalDateTime;

public class PlantaEolica extends PlantaRenovable {
	
	private double alturaTorres;
	private double velocidadMinDeViento;
	private int cantAerogeneradores;

	public PlantaEolica(String nombre, String direccion, 
            double capMax, LocalDateTime fecha, 
            String fuentePrimaria, double eficienciaPromedio,
            double alturaTorres, double velocidadMinDeViento, 
            int cantAerogeneradores) {
		super (nombre, direccion, capMax, fecha, fuentePrimaria, eficienciaPromedio);
		this.alturaTorres = alturaTorres;
		this.velocidadMinDeViento = velocidadMinDeViento;
		this.cantAerogeneradores = cantAerogeneradores;
	}
	
	public String generarReportePocoViento() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("- ").append(this.getNombrePlanta()).append(":  ").append(this.velocidadMinDeViento).append(" m/s\n");
	    double[] vientosPorHora = this.calcularMaxVientoEnHorasSinMinima();

	    for (int h = 0; h < 24; h++) {
	        if (vientosPorHora[h] >= 0) {
	            sb.append("       ");
	            if (h < 10) {
	                sb.append("0");
	            }
	            sb.append(h).append(": ").append(vientosPorHora[h]).append(" m/s\n");
	        }
	    }
	    return sb.toString();
	}

	private double[] calcularMaxVientoEnHorasSinMinima() {
	    double[] estadoHoras = new double[24];
	    java.util.Arrays.fill(estadoHoras, -1.0); // sin registros

	    for (RegistroMonitoreo reg : this.getRegistros()) {
	        if (reg instanceof MonitoreoPlantaEolica) {
	            MonitoreoPlantaEolica mon = (MonitoreoPlantaEolica) reg;
	            int hora = mon.getFechaHora().getHour();
	            double viento = mon.getVelocidadVientoPromedio();

	            if (viento >= this.velocidadMinDeViento) {
	                estadoHoras[hora] = -2.0; // se descarta porque tiene la velocidad minima
	            } else if (estadoHoras[hora] != -2.0 && viento > estadoHoras[hora]) {
	                estadoHoras[hora] = viento;
	            }
	        }
	    }
	    return estadoHoras;
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
