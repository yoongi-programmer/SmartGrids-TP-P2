package SmartGrid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Planta implements Comparable<Planta>{     
	private String nombre;
	private String direccion;
	private double capMax;
	private boolean esActiva;
	private LocalDateTime fechaInicioActividad;
	private ArrayList<RegistroMonitoreo> registros;
	
	private ArrayList<LocalDateTime[]> periodosInactividad;
	private LocalDateTime fechaInicioInactividad;
	
	public Planta( String nombre, String direccion, double capMax, LocalDateTime fecha) {
		if(nombre == null || nombre.length() == 0 || nombre.length() > 50) {
			throw new RuntimeException("Debe ingresar un nombre válido");
		}
		if(direccion == null || direccion.length() == 0 || direccion.length() > 100) {
			throw new RuntimeException("Debe ingresar una direccion válido");
		}
		if(capMax < 0) {
			throw new RuntimeException("Los capacidad máxima no pueden ser < 0");
		}

		this.nombre = nombre;
		this.direccion = direccion;
		this.capMax = capMax;
		this.esActiva = true; //al instanciarla por defecto esta activa
		this.fechaInicioActividad = fecha;
		this.registros = new ArrayList<RegistroMonitoreo>();
		this.periodosInactividad = new ArrayList<>();
	}
	public double consultarTotalGenerado() {
		return calcularTotalGenerado();
	}
	private double calcularTotalGenerado() {
		double energiaTotalGenerada = 0;
		for(RegistroMonitoreo reg : registros) {
			energiaTotalGenerada += reg.getEnergia();
		}
		return energiaTotalGenerada;
	}
	
	public int[] consultarIncidenciasPorHora() {
		return calcularIncidenciasPorHora();
	}
	private int[] calcularIncidenciasPorHora() {
	    int[] incidencias = new int[24];
	    
	    for (RegistroMonitoreo r : registros) {
	        int hora = r.getFechaHora().getHour();
	        incidencias[hora] += r.getIncidencias();
	    }
	    
	    return incidencias;
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
			int horaRegistro = r.getFechaHora().getHour();
			LocalDate fechaRegistro = r.getFechaHora().toLocalDate();
			
			int horaRNuevo = fechaHoraRegistroNuevo.getHour();
			LocalDate fechaRNuevo = fechaHoraRegistroNuevo.toLocalDate();
			
			if(horaRegistro == horaRNuevo && fechaRegistro.compareTo(fechaRNuevo) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public void activar(LocalDateTime fechaHora) {
		cambiarEstadoActivar(fechaHora);
	}
	private void cambiarEstadoActivar(LocalDateTime fechaHora) {
        if (!this.esActiva) {
            this.esActiva = true;
            if (this.fechaInicioInactividad != null) {
                LocalDateTime[] periodo = {this.fechaInicioInactividad, fechaHora};
                this.periodosInactividad.add(periodo);
                this.fechaInicioInactividad = null;
            }
        }
    }
	
	public void desactivar(LocalDateTime fechaHora) {
		cambiarEstadoDesactivar(fechaHora);
	}
	private void cambiarEstadoDesactivar(LocalDateTime fechaHora) {
        if (this.esActiva) {
            this.esActiva = false;
            this.fechaInicioInactividad = fechaHora;
        }
    }
	
	public boolean consultarEstadoInactivoEn(LocalDateTime fechaHora) {
		return estabaInactivaEn(fechaHora);
	}
	private boolean estabaInactivaEn(LocalDateTime fechaHora) {
        if (!this.esActiva && this.fechaInicioInactividad != null) {
            if (!fechaHora.isBefore(this.fechaInicioInactividad)) {
                return true;
            }
        }
        for (LocalDateTime[] periodo : this.periodosInactividad) {
            LocalDateTime inicio = periodo[0];
            LocalDateTime fin = periodo[1];
            if (!fechaHora.isBefore(inicio) && !fechaHora.isAfter(fin)) {
                return true;
            }
        }
        return false;
    }
	
	public boolean cumplioCuotaDeGeneracion() {
		return cumplioCuota();
	}
	
	private boolean cumplioCuota() {
		if(registros.isEmpty()) {
			return false;
		}
		
		boolean cumplioCuota = false;
		
		for(RegistroMonitoreo registro:registros) {
			cumplioCuota = cumplioCuota || cumplioCuotaEnRegistro(registro);
		}
		
		return cumplioCuota;
	}
	
	protected boolean cumplioCuotaEnRegistro(RegistroMonitoreo reg) {
		return reg.getEnergia() >= calcularCuotaMinima();
	} 
	
	public double consultarCuotaMinima() {
		return calcularCuotaMinima();
	}
	
	protected abstract double calcularCuotaMinima();
	
	public int[] consultarHistograma(LocalDate desde, LocalDate hasta) {
		return calcularHistogramaCumplimiento(desde, hasta);
	}
	
	private int[] calcularHistogramaCumplimiento(LocalDate desde, LocalDate hasta) {
	    int[] histograma = new int[24]; //24hs: 0-23

	    for (RegistroMonitoreo reg : this.registros) {
	        LocalDate fechaReg = reg.getFechaHora().toLocalDate();

	        boolean enRango = !fechaReg.isBefore(desde) && !fechaReg.isAfter(hasta);

	        if (enRango && this.cumplioCuotaEnRegistro(reg)) {
	            int hora = reg.getFechaHora().getHour(); // Devuelve entre 0 y 23
	            histograma[hora]++;
	        }
	    }
	    return histograma;
	}
	
	public int compareTo(Planta otraPlanta) {
		return Double.compare(otraPlanta.calcularTotalGenerado(), this.calcularTotalGenerado());
	}
	
	public String getNombre() {
		return this.nombre;
	}
	public double getCapMax() {
		return this.capMax;
	}
	public boolean getActiva() {
		return this.esActiva;
	}
	
	public String getDireccion() {
        return this.direccion;
    }
	
	public String getFechaString() {
		return fechaInicioActividad.toString();
	}
	public LocalDateTime getFecha() {
		return fechaInicioActividad;
	}
	
	protected ArrayList<RegistroMonitoreo> getRegistros() {
		return registros;
	}
	
	@Override
	public String toString() {
	    String estado = this.esActiva ? "ACTIVA" : "INACTIVA";
	    StringBuilder sb = new StringBuilder();
	    sb.append("Planta: ").append(this.nombre).append(" [").append(estado).append("]").append(" | Dir: ").append(this.direccion).append(" | CapMax: ").append(this.capMax)
	      .append(" | Inicio: ").append(this.fechaInicioActividad).append(" | Total Generado: ").append(this.calcularTotalGenerado()).append(" | Monitoreos: ").append(this.registros);  
	    return sb.toString();
	}
	
}
