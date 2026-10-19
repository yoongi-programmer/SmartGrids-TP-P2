package SmartGrid;

import java.time.LocalDate;
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
}
