package SmartGrid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartGrids implements ISmartGrids {
	Map<String, Planta> Plantas = new HashMap<>();
	
	@Override
	public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, 
									double eficienciaPromedio, double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
		PlantaSolar p = new PlantaSolar(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, horasRadiacionDiaria, 
										cantidadPaneles, superficieCubierta);
		Plantas.put(nombre, p);
	}

	@Override
	public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
		
		PlantaEolica p = new PlantaEolica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, alturaTorres, 
				velocidadMinimaViento, cantidadAerogeneradores);
		Plantas.put(nombre, p);

	}

	@Override
	public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadTurbinas, double capacidadEmbalse) {
		PlantaHidraulica p = new PlantaHidraulica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio, capacidadEmbalse, 
				cantidadTurbinas);
		Plantas.put(nombre, p);

	}

	@Override
	public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
			double capacidadAlmacenamientoCombustible) {
		
		 PlantaTermica p = new PlantaTermica(nombre, direccion, capacidadMaxima, fechaHoraPuestaEnFuncionamiento, tipoCombustible, objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
		Plantas.put(nombre, p);

	}

	@Override
	public List<String> consultarPlantas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double capacidadMinimaCuota(String nombre) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void activarPlanta(String nombre, String fechaHora) {
		// TODO Auto-generated method stub

	}

	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean estaActiva(String nombre) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double velocidadViento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double co2Emitido, double combustibleUtilizado) {
		// TODO Auto-generated method stub

	}

	@Override
	public double totalGenerado(String nombre) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] incidenciasPorHora(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
		// TODO Auto-generated method stub
		return null;
	}

}
