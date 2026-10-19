package SmartGrid;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Planta {     
	private String nombre;
	private String direccion;
	private double capMax;
	private boolean esActiva;
	private LocalDateTime fechaInicioActividad;
	private ArrayList<RegistroMonitoreo> registros;
	
	public Planta( String nombre, String direccion, double capMax, LocalDateTime fecha) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.capMax = capMax;
		this.esActiva = true; //al instanciarla por defecto esta activa
		this.fechaInicioActividad = fecha;
		this.registros = new ArrayList<RegistroMonitoreo>();
	}
	
	public String getNombrePlanta() {
		return this.nombre;
	}
	public double getCapMax() {
		return this.capMax;
	}
	public boolean estaActiva() {
		return this.esActiva;
	}
	public String getNombre() {
		return this.nombre;
	}
	
	protected ArrayList<RegistroMonitoreo> getRegistros() {
		return registros;
	}

	public void agregarRegistro(RegistroMonitoreo registro) {
		
		if(esAnteriorAInicioActividad(registro.getFechaHora())) {
			throw new RuntimeException ("Falla en el registro. La fecha ingresada es anterior a la puesta en funcionamiento de la Planta.");
		}
		
		if(existeRegistroMismoHorario(registro.getFechaHora())) {
			throw new RuntimeException ("Falla en el registro. Ya existe un registro para el horario indicado.");
		}
		
		registros.add(registro);
	}
	
	private boolean esAnteriorAInicioActividad(LocalDateTime fechaR) {
		if(fechaInicioActividad.compareTo(fechaR)>0) {
			return true;
		}
		return false;
	}
	
	private boolean existeRegistroMismoHorario(LocalDateTime fechaHoraRegistroNuevo) {
		for(RegistroMonitoreo r: registros) {
			if(r.getFechaHora().compareTo(fechaHoraRegistroNuevo) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean cumplioCuotaDeGeneracion() {
		return cumplioCuota();
	}
	
	
	public double consultarCuotaMinima() {
		return calcularCuotaMinima();
	}
	
	protected abstract double calcularCuotaMinima();
	
	protected boolean cumplioCuota() {
		if(registros.isEmpty()) {
			return false;
		}
		
		double cuotaMinima = calcularCuotaMinima();
		boolean cumplioCuota = true;
		
		for(RegistroMonitoreo registro:registros) {
			cumplioCuota = cumplioCuota && (registro.getEnergia() >= cuotaMinima);
		}
		
		return cumplioCuota;
	}
}
