package SmartGrid;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SmartGrids implements ISmartGrids {
	public LinkedHashMap<String, Planta> plantas;
	
	public SmartGrids() {
		this.plantas = new LinkedHashMap<String, Planta>();
	}
	/*
     * 1.a) Registran una planta con sus datos y caracteristicas.
     * 
     * Por defecto la planta se crea en estado activa.  
     * 
     * @throws RuntimeException si ya existe ese nombre o falta o es 
     *         inaceptable algun dato.
     */
	//Registo Plantas-----------------------------------------------------------------------------------
	
	@Override
	public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, 
									double eficienciaPromedio, double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
		
		this.controlarPlanta(nombre);
		PlantaSolar p = new PlantaSolar(nombre, direccion, capacidadMaxima, convertirFechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio, horasRadiacionDiaria, 
										cantidadPaneles, superficieCubierta);
		plantas.put(nombre, p);
	}

	@Override
	public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
		
		this.controlarPlanta(nombre);
		PlantaEolica p = new PlantaEolica(nombre, direccion, capacidadMaxima, convertirFechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio, alturaTorres, 
				velocidadMinimaViento, cantidadAerogeneradores);
		plantas.put(nombre, p);

	}

	@Override
	public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadTurbinas, double capacidadEmbalse) {
		
		this.controlarPlanta(nombre);
		PlantaHidraulica p = new PlantaHidraulica(nombre, direccion, capacidadMaxima, convertirFechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio, capacidadEmbalse, 
				cantidadTurbinas);
		plantas.put(nombre, p);

	}

	@Override
	public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
			double capacidadAlmacenamientoCombustible) {
		
		this.controlarPlanta(nombre);
		PlantaTermica p = new PlantaTermica(nombre, direccion, capacidadMaxima, convertirFechaHora(fechaHoraPuestaEnFuncionamiento), tipoCombustible, objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
		plantas.put(nombre, p);

	}
	//Consultar Plantas-----------------------------------------------------------------------------------
	@Override
	public List<String> consultarPlantas() {
		List<String> lista = new ArrayList<>();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
		for(Map.Entry<String, Planta> entry : plantas.entrySet()) {
			Planta planta = entry.getValue();
			
			String estado;
			if(planta.getActiva()) { estado = "ACTIVA"; } else { estado = "INACTIVA"; }
			
			StringBuilder sb = new StringBuilder();
	        sb.append(planta.getNombrePlanta()).append(" (").append(estado).append("): ").append(planta.getFecha().format(formatter)); //construye el string {NOMBRE} (ACTIVA/INACTIVA): 18-10-2026T22:00:00
			
			lista.add(sb.toString());
		}
		return lista;
	}

	   /*
     * 2) Consulta cuanta energia debe generar la planta en una hora para
     * cumplir la cuota. En las solares se tiene en cuenta la radiacion;
     * en las eolicas e hidraulicas, la eficiencia. Las termicas deben
     * generar la mitad de su capacidad y emitir menos CO2 que el objetivo
     * por hora.
     *
     * @throws RuntimeException si la planta no existe
     */
	@Override
	public double capacidadMinimaCuota(String nombre) {
		if(!plantas.containsKey(nombre)) {
			throw new RuntimeException("La planta consultada no esta registrada.");
		}
		
		return plantas.get(nombre).consultarCuotaMinima();
	}

	@Override
	public void activarPlanta(String nombre, String fechaHora) {
		Planta planta = plantas.get(nombre);
        if (planta == null) {
            throw new RuntimeException("La planta '" + nombre + "' no existe");
        }
        LocalDateTime fecha = LocalDateTime.parse(fechaHora);
        planta.cambiarEstadoActivar(fecha);
    }

	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
		Planta planta = plantas.get(nombre);
        if (planta == null) {
            throw new RuntimeException("La planta '" + nombre + "' no existe");
        }
        LocalDateTime fecha = LocalDateTime.parse(fechaHora);
        planta.cambiarEstadoDesactivar(fecha);
    }

	@Override
	public boolean estaActiva(String nombre) {
		Planta planta = plantas.get(nombre);
        if (planta == null) {
            throw new RuntimeException("La planta '" + nombre + "' no existe");
        }
        return planta.getActiva();
    }

    /*
     * 4) crea un registro de monitoreo en la planta especificada.
     * 
     * Anota la energia y las incidencias de una hora en una planta.
     * y si corresponde, la velocidad del viento o emision de CO2 y 
     * uso de combuistible, segun corresponda.
     *
     * @throws RuntimeException si la planta no existe o esta inactiva; 
     *         si falta la fechaHora, es anterior a la puesta en marcha o ya 
     *         hay un registro para esa hora; o si la energia o las incidencias
     *         son negativas, etc.
     */
	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
			
		boolean plantaRegistrada = plantas.containsKey(nombre);

		if(nombre == null || !plantaRegistrada) {
			throw new RuntimeException ("Falla en el registro. La planta no está registrada.");
		}
		
		if(!plantas.get(nombre).getActiva()) {
			throw new RuntimeException ("Falla en el registro. La planta no está activa.");
		}
		
		RegistroMonitoreo nuevoRegistro = new RegistroMonitoreo(convertirFechaHora(fechaHora), energiaGenerada, incidencias);
		
		plantas.get(nombre).agregarRegistro(nuevoRegistro);
	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double velocidadViento) {
		boolean plantaRegistrada = plantas.containsKey(nombre);

		if(!plantaRegistrada) {
			throw new RuntimeException ("Falla en el registro. La planta no está registrada.");
		}
		
		if(!plantas.get(nombre).getActiva()) {
			throw new RuntimeException ("Falla en el registro. La planta no está activa.");
		}

		if(!(plantas.get(nombre) instanceof PlantaEolica)) {
			throw new RuntimeException ("Falla en el registro. La planta debe ser Eolica.");
		}
		
		MonitoreoPlantaEolica nuevoRegistro = new MonitoreoPlantaEolica(convertirFechaHora(fechaHora), energiaGenerada, incidencias, velocidadViento);
		
		plantas.get(nombre).agregarRegistro(nuevoRegistro);

	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double co2Emitido, double combustibleUtilizado) {
		boolean plantaRegistrada = plantas.containsKey(nombre);

		if(!plantaRegistrada) {
			throw new RuntimeException ("Falla en el registro. La planta no está registrada.");
		}
		
		if(!plantas.get(nombre).getActiva()) {
			throw new RuntimeException ("Falla en el registro. La planta no está activa.");
		}
		
		if(!(plantas.get(nombre) instanceof PlantaTermica)) {
			throw new RuntimeException ("Falla en el registro. La planta debe ser Termica.");
		}
		
		MonitoreoPlantaTermica nuevoRegistro = new MonitoreoPlantaTermica (convertirFechaHora(fechaHora), energiaGenerada, incidencias, co2Emitido, combustibleUtilizado);
		
		plantas.get(nombre).agregarRegistro(nuevoRegistro);

	}

	@Override
	public double totalGenerado(String nombre) {
		if(plantas.containsKey(nombre)) {
			return plantas.get(nombre).calcularTotalGenerado();
		}else {
			throw new RuntimeException("Ya existe una planta registrada con el nombre: " + nombre);
		}
	}

	@Override
	public int[] incidenciasPorHora(String nombre) {
	    if (!plantas.containsKey(nombre)) {
	        throw new RuntimeException("La planta '" + nombre + "' no existe");
	    }
	    
	    int[] incidencias = new int[24];
	    Planta planta = plantas.get(nombre);
	    
	    for (RegistroMonitoreo r : planta.getRegistros()) {
	        int hora = r.getFechaHora().getHour();
	        incidencias[hora] += r.getIncidencias();
	    }
	    
	    return incidencias;
	}

	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
     * 8) Calcula y devulve la cantidad de plantas renovables por fuente 
     * primaria, ordenadas por tipo de fuente.
     */
	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
		Map<String, Integer> plantasRenovablesPorFuente = new HashMap<String, Integer>();
		
		for(Planta p: plantas.values()) {
			if(p instanceof PlantaRenovable) {
				int contador;
				String fuenteP = ((PlantaRenovable) p).getFuentePrimaria();
				if(plantasRenovablesPorFuente.containsKey(fuenteP)) {
					contador = plantasRenovablesPorFuente.get(fuenteP);
					contador++;
					plantasRenovablesPorFuente.put(fuenteP, contador);
				}
				else {
					plantasRenovablesPorFuente.put(fuenteP, 1);
				}
			}
		}
	
		return plantasRenovablesPorFuente;
	}

	  /*
     * 9) Devuelve nombres de las plantas que nunca cumplieron la cuota, 
     * incluso si no tienen monitoreos.
     */
	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
		List<String> plantasQueNuncaCumplieronCuota = new ArrayList<String>();
		
		for(Planta planta : plantas.values()) {
			if(!planta.cumplioCuotaDeGeneracion()) {
				plantasQueNuncaCumplieronCuota.add(planta.getNombre());
			}
		}
		
		return plantasQueNuncaCumplieronCuota;
	}

	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int limpiarMonitoreosSinDatos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
		List<Planta> listaPlantas = new ArrayList<>(this.plantas.values());
		Collections.sort(listaPlantas);
		List<String> resultado = new ArrayList<>();
		
		for(Planta p : listaPlantas) {
			StringBuilder sb = new StringBuilder();
			sb.append(p.getNombre()).append(": ").append(p.calcularTotalGenerado());
			resultado.add(sb.toString());
		}
		return resultado;
	}

	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);
	    List<String> inactivas = new ArrayList<>();
	    
	    for (Planta planta : plantas.values()) {
	        if (planta.estabaInactivaEn(fecha)) {
	            inactivas.add(planta.getNombre());
	        }
	    }
	    
	    return inactivas;
	}

	private LocalDateTime convertirFechaHora(String fecha) {
		try{
			LocalDateTime fechaHora = LocalDateTime.parse(fecha);
			return fechaHora;
		} catch (RuntimeException e) {
			System.out.println("El formato de la fecha y hora ingresada debe ser: yyyy/MM/dd'T'HH:mm:ss");
			throw e;
		}
	}
	
	private LocalDate convertirFecha(String fecha) {
		try{
			LocalDate fechaFormateada = LocalDate.parse(fecha);
			return fechaFormateada;
		} catch (RuntimeException e) {
			System.out.println("El formato de la fecha y hora ingresada debe ser: yyyy/MM/dd");
			throw e;
		}
	}
	
	private void controlarPlanta(String nombre) {
		if(plantas.containsKey(nombre)) {
			throw new RuntimeException("Ya existe una planta registrada con el nombre: " + nombre);
		}
	}
	
}
