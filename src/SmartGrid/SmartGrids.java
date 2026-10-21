package SmartGrid;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SmartGrids implements ISmartGrids {
	public LinkedHashMap<String, Planta> plantas;
	
	public SmartGrids() {
		this.plantas = new LinkedHashMap<String, Planta>();
	}
	/*
     * 1.a) Registran una planta con sus datos y caracteristicas. Por defecto la planta se crea en estado activa.  
     * @throws RuntimeException si ya existe ese nombre o falta o es inaceptable algun dato.
     */
	//1.1 Registo Plantas-----------------------------------------------------------------------------------
	
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
	//1.2 Consultar Plantas-----------------------------------------------------------------------------------
	@Override
	public List<String> consultarPlantas() {
		List<String> lista = new ArrayList<>();
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
		for(Map.Entry<String, Planta> entry : plantas.entrySet()) {
			Planta planta = entry.getValue();
			
			String estado;
			if(planta.getActiva()) { estado = "ACTIVA"; } else { estado = "INACTIVA"; }
			
			StringBuilder sb = new StringBuilder();
	        sb.append(planta.getNombre()).append(" (").append(estado).append("): ").append(planta.getFecha().format(formatter)); //construye el string {NOMBRE} (ACTIVA/INACTIVA): 18-10-2026T22:00:00
			
			lista.add(sb.toString());
		}
		return lista;
	}

	   /*
     * 2) Consulta cuanta energia debe generar la planta en una hora para cumplir la cuota. En las solares se tiene en cuenta la radiacion;
     * en las eolicas e hidraulicas, la eficiencia. Las termicas deben generar la mitad de su capacidad y emitir menos CO2 que el objetivo por hora.
     * @throws RuntimeException si la planta no existe
     */
	//2. Consultar capacidad mínima de cuota----------------------------------------------------------------------
	@Override
	public double capacidadMinimaCuota(String nombre) {
		if(!plantas.containsKey(nombre)) {
			throw new RuntimeException("La planta consultada no esta registrada.");
		}
		
		return plantas.get(nombre).consultarCuotaMinima();
	}
	
	//3. Actualizar estado de planta-----------------------------------------------------------------------------------
	@Override
	public void activarPlanta(String nombre, String fechaHora) {
		Planta planta = plantas.get(nombre);
        if (planta == null) {
            throw new RuntimeException("La planta '" + nombre + "' no existe");
        }
        LocalDateTime fecha = LocalDateTime.parse(fechaHora);
        planta.activar(fecha);
    }
	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
		Planta planta = plantas.get(nombre);
        if (planta == null) {
            throw new RuntimeException("La planta '" + nombre + "' no existe");
        }
        LocalDateTime fecha = LocalDateTime.parse(fechaHora);
        planta.desactivar(fecha);
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
     * Anota la energia y las incidencias de una hora en una planta. y si corresponde, la velocidad del viento o emision de CO2 y uso de combuistible, segun corresponda.
     * @throws RuntimeException si la planta no existe o esta inactiva; 
     *   si falta la fechaHora, es anterior a la puesta en marcha o ya hay un registro para esa hora; o si la energia o las incidencias son negativas, etc.
     */
	//4. Registo Monitoreo-----------------------------------------------------------------------------------
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
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
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
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double co2Emitido, double combustibleUtilizado) {
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
	//5. Consultar total generado-----------------------------------------------------------------------------------
	@Override
	public double totalGenerado(String nombre) {
		if(plantas.containsKey(nombre)) {
			return plantas.get(nombre).consultarTotalGenerado();
		}else {
			throw new RuntimeException("Ya existe una planta registrada con el nombre: " + nombre);
		}
	}
	//6. Consulta total incidencias-----------------------------------------------------------------------------------
	@Override
	public int[] incidenciasPorHora(String nombre) {
	    if (!plantas.containsKey(nombre)) {
	        throw new RuntimeException("La planta '" + nombre + "' no existe");
	    }
	    return plantas.get(nombre).consultarIncidenciasPorHora();
	}
		
	//7. Histograma-----------------------------------------------------------------------------------
	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
	    if (nombre == null || !plantas.containsKey(nombre)) {
	        throw new RuntimeException("La planta no existe: " + nombre);
	    }
	    LocalDate desde = convertirFecha(fechaDesde);
	    LocalDate hasta = convertirFecha(fechaHasta);

	    if (desde.isAfter(hasta)) {
	        throw new RuntimeException("La fecha 'desde' no puede ser posterior a 'hasta'.");
	    }

	    return plantas.get(nombre).consultarHistograma(desde, hasta);
	}

	//8. Cantidad plantas por fuente-----------------------------------------------------------------------------------
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
	
	//9. Plantas que nunca cumplieron cuota-----------------------------------------------------------------------------
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
	//10. Plantas eolicas sin velocidad mínima--------------------------------------------------------------------------
	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
		StringBuilder sb = new StringBuilder();
		sb.append("Plantas Eolicas: Horas con poco viento \n");
		
		for(Planta planta : plantas.values()) {
			if(planta instanceof PlantaEolica) {
				PlantaEolica eolica = (PlantaEolica) planta;
				sb.append(eolica.generarReportePocoViento());
			}
		}
		return sb.toString();
	}
	//11. Limpiar registros---------------------------------------------------------------------------------------------
	@Override
	public int limpiarMonitoreosSinDatos() {
	    int totalBorrados = 0;
	    for (Planta planta : plantas.values()) {
	        Iterator<RegistroMonitoreo> it = planta.getRegistros().iterator();
	        while (it.hasNext()) {
	            RegistroMonitoreo r = it.next();
	            if (r.getEnergia() == 0 && r.getIncidencias() == 0) {
	                it.remove();
	                totalBorrados++;
	            }
	        }
	    }
	    
	    return totalBorrados;
	}
	//12. Plantas ordenadas por energia generada------------------------------------------------------------------------
	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
		List<Planta> listaPlantas = new ArrayList<>(this.plantas.values());
		Collections.sort(listaPlantas);
		List<String> resultado = new ArrayList<>();
		
		for(Planta p : listaPlantas) {
			StringBuilder sb = new StringBuilder();
			sb.append(p.getNombre()).append(": ").append(p.consultarTotalGenerado());
			resultado.add(sb.toString());
		}
		return resultado;
	}
	//13. Plantas inactivas por hora-----------------------------------------------------------------------------------
	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);
	    List<String> inactivas = new ArrayList<>();
	    
	    for (Planta planta : plantas.values()) {
	        if (planta.consultarEstadoInactivoEn(fecha)) {
	            inactivas.add(planta.getNombre());
	        }
	    }
	    
	    return inactivas;
	}
	//Utilidades-------------------------------------------------------------
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append(" ===[ Sistema SmartGrids ]===\n");
	    sb.append(" Total de plantas registradas: ").append(plantas.size()).append("\n");
	    
	    if (plantas.isEmpty()) {
	        sb.append(" (Sin plantas registradas)\n");
	    } else {	 
	        for (Planta p : plantas.values()) {	          
	            sb.append(p.toString()).append("\n");
	        }
	    }
	    return sb.toString();
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
