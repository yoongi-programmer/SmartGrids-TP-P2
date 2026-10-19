package SmartGrid;

import java.util.ArrayList;

public abstract class Planta {     
	//private int idPlanta;
	private String nombre;
	private String direccion;
	private double capMax;
	private boolean esActiva;
	private String fecha;
	private ArrayList<RegistroMonitoreo> registros;
	
	public Planta(String nombre, String direccion, double capMax, String fecha) {
		//this.idPlanta = idPlanta;
		this.nombre = nombre;
		this.direccion = direccion;
		this.capMax = capMax;
		this.esActiva = true; //al instanciarla por defecto esta activa
		this.fecha = fecha;
	}
	
	public String getNombrePlanta() {
		return this.nombre;
	}
	public double getCapMax() {
		return this.capMax;
	}
	public boolean getActiva() {
		return this.esActiva;
	}
	public String getFecha() {
		return this.fecha;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null) return false;
	    if (getClass() != obj.getClass()) return false;
	    Planta other = (Planta) obj;
	    
	    if (nombre == null) {
	        if (other.nombre != null) return false;
	    } else if (!nombre.equals(other.nombre)) {
	        return false;
	    }
	    return true;
	}

	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
	    return result;
	}
}
