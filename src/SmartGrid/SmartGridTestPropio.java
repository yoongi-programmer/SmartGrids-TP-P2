package SmartGrid;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class SmartGridTestPropio {

    private static final String PUESTA = "2020-01-01T00:00:00";
    private static final String DIA1 = "2026-03-01";
    private static final String DIA2 = "2026-03-02";
    //private static final String DIA3 = "2026-03-03";

    private ISmartGrids sistema;

    @Before
    public void setUp() {
        sistema = new SmartGrids();
        // Cuota Solar1 = 1000 * 0.5 * 12 / 24 = 250.0
        sistema.registrarPlantaSolar("Solar1", "Ruta 1", 1000, PUESTA, "Sol", 0.5, 5000, 2000, 12);
        // Cuota Eolica1 = 400 * 0.5 = 200.0 (velMinima = 5.0)
        sistema.registrarPlantaEolica("Eolica1", "Ruta 2", 400, PUESTA, "Viento", 0.5, 10, 80, 5);
        // Cuota Hidro1 = 800 * 0.25 = 200.0
        sistema.registrarPlantaHidraulica("Hidro1", "Ruta 3", 800, PUESTA, "Agua", 0.25, 4, 1000);
        // Cuota Termica1 = 600 / 2 = 300.0 y CO2 < 50.0
        sistema.registrarPlantaTermica("Termica1", "Ruta 4", 600, PUESTA, "Gas", 50, 900);
    }

    // =========================================================================
    // 1. PRUEBAS DE REGISTRO, CONSULTA Y VALIDACIÓN DE IREP EN PLANTAS
    // =========================================================================

    @Test
    public void consultarPlantasEnSistemaVacioYCambioDeEstadoEnTexto() {
        ISmartGrids sistemaVacio = new SmartGrids();
        assertTrue(sistemaVacio.consultarPlantas().isEmpty());

        // Desactivamos dos plantas y verificamos que el texto cambie a INACTIVA manteniendo el orden
        sistema.desactivarPlanta("Eolica1", DIA1 + "T10:00:00");
        sistema.desactivarPlanta("Termica1", DIA1 + "T10:00:00");

        List<String> esperado = Arrays.asList(
            "Solar1 (ACTIVA): " + PUESTA,
            "Eolica1 (INACTIVA): " + PUESTA,
            "Hidro1 (ACTIVA): " + PUESTA,
            "Termica1 (INACTIVA): " + PUESTA
        );
        assertEquals(esperado, sistema.consultarPlantas());
    }

    @Test
    public void bloqueaNombresDuplicadosEnTodosLosTiposDePlanta() {
        // Verifica que ninguno de los 4 métodos de registro permita pisar un nombre existente
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaSolar("Solar1", "Dir", 100, PUESTA, "Sol", 0.5, 100, 10, 10));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaEolica("Eolica1", "Dir", 100, PUESTA, "Viento", 0.5, 10, 50, 5));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaHidraulica("Hidro1", "Dir", 100, PUESTA, "Agua", 0.5, 2, 500));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaTermica("Termica1", "Dir", 100, PUESTA, "Gas", 20, 500));
    }

    @Test
    public void rechazaPlantasConDatosInvalidosOFechasMalFormateadas() {
        // Fecha con formato inválido
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaSolar("SolarNueva", "Dir", 100, "01/01/2020", "Sol", 0.5, 100, 10, 10));
        
        // Nombre nulo o capacidad negativa (si validan IREP en constructores/registro)
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaSolar(null, "Dir", 100, PUESTA, "Sol", 0.5, 100, 10, 10));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarPlantaSolar("SolarNeg", "Dir", -50, PUESTA, "Sol", 0.5, 100, 10, 10));
    }

    // =========================================================================
    // 2. EXCEPCIONES EN CONSULTAS SOBRE PLANTAS INEXISTENTES
    // =========================================================================

    @Test
    public void lanzaExcepcionAlConsultarOperacionesDePlantaInexistente() {
        assertThrows(RuntimeException.class, () -> sistema.capacidadMinimaCuota("Inexistente"));
        assertThrows(RuntimeException.class, () -> sistema.activarPlanta("Inexistente", DIA1 + "T10:00:00"));
        assertThrows(RuntimeException.class, () -> sistema.desactivarPlanta("Inexistente", DIA1 + "T10:00:00"));
        assertThrows(RuntimeException.class, () -> sistema.estaActiva("Inexistente"));
        assertThrows(RuntimeException.class, () -> sistema.totalGenerado("Inexistente"));
        assertThrows(RuntimeException.class, () -> sistema.incidenciasPorHora("Inexistente"));
        assertThrows(RuntimeException.class, () -> sistema.histogramaCumplimiento("Inexistente", DIA1, DIA2));
    }

    // =========================================================================
    // 3. MONITOREOS: CASOS BORDE, SOBRECARGAS CRUZADAS Y PARÁMETROS NEGATIVOS
    // =========================================================================

    @Test
    public void permiteMonitoreoEnMismoInstanteDePuestaEnMarcha() {
        // Caso borde: registrar exactamente en el segundo 0 de la puesta en funcionamiento (no es anterior)
        sistema.registrarMonitoreo("Solar1", PUESTA, 250, 0);
        assertEquals(250.0, sistema.totalGenerado("Solar1"), 0.01);
    }

    @Test
    public void rechazaSobrecargasDeMonitoreoCruzadasEInvalidas() {
        String fecha = DIA1 + "T08:00:00";

        // Monitoreo térmico (6 parámetros) sobre planta solar o eólica -> Debe fallar
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarMonitoreo("Solar1", fecha, 100, 0, 10, 5));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarMonitoreo("Eolica1", fecha, 100, 0, 10, 5));

        // Monitoreo eólico (5 parámetros) sobre planta térmica o hidráulica -> Debe fallar
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarMonitoreo("Termica1", fecha, 100, 0, 15));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarMonitoreo("Hidro1", fecha, 100, 0, 15));

        // Energía negativa o incidencias negativas -> Debe fallar según interfaz
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarMonitoreo("Solar1", fecha, -10, 0));
        assertThrows(RuntimeException.class, () -> 
            sistema.registrarMonitoreo("Solar1", fecha, 100, -1));
    }

    // =========================================================================
    // 4. HISTOGRAMA E INCIDENCIAS: HORAS EXTREMAS (00 y 23) Y BORDES DE FECHA
    // =========================================================================

    @Test
    public void histogramaEIncidenciasEnHorasExtremas00y23() {
        // Probamos los límites del arreglo [0..23]
        sistema.registrarMonitoreo("Solar1", DIA1 + "T00:00:00", 250, 3); // Hora 0: cumple
        sistema.registrarMonitoreo("Solar1", DIA1 + "T23:00:00", 300, 5); // Hora 23: cumple
        sistema.registrarMonitoreo("Solar1", DIA2 + "T23:00:00", 249.9, 2); // Hora 23: NO cumple (por 0.1)

        int[] hist = sistema.histogramaCumplimiento("Solar1", DIA1, DIA2);
        assertEquals(1, hist[0]);
        assertEquals(1, hist[23]);

        int[] inc = sistema.incidenciasPorHora("Solar1");
        assertEquals(3, inc[0]);
        assertEquals(7, inc[23]); // 5 + 2
    }

    // =========================================================================
    // 5. PUNTO 9: PLANTAS QUE NUNCA CUMPLIERON CUOTA (CON MÚLTIPLES REGISTROS)
    // =========================================================================

    @Test
    public void plantaQueCumpleUnaSolaVezEntreVariosMonitoreosNoApareceEnNuncaCumplieron() {
        // Este test verifica que no se use && entre todos los registros, sino || (al menos una vez)
        // Solar1 falla 2 veces y cumple 1 sola vez:
        sistema.registrarMonitoreo("Solar1", DIA1 + "T01:00:00", 10, 0);  // No cumple
        sistema.registrarMonitoreo("Solar1", DIA1 + "T02:00:00", 250, 0); // SÍ cumple
        sistema.registrarMonitoreo("Solar1", DIA1 + "T03:00:00", 50, 0);  // No cumple

        // Termica1 genera suficiente energía pero falla en CO2 dos veces, y cumple en el 3ro:
        sistema.registrarMonitoreo("Termica1", DIA1 + "T01:00:00", 400, 0, 50, 10); // No cumple (CO2 == 50)
        sistema.registrarMonitoreo("Termica1", DIA1 + "T02:00:00", 200, 0, 10, 10); // No cumple (Energia < 300)
        sistema.registrarMonitoreo("Termica1", DIA1 + "T03:00:00", 300, 0, 49.9, 10); // SÍ cumple

        // Hidro1 tiene varios registros y jamás cumple:
        sistema.registrarMonitoreo("Hidro1", DIA1 + "T01:00:00", 100, 0);
        sistema.registrarMonitoreo("Hidro1", DIA1 + "T02:00:00", 199.9, 0);

        // Eolica1 no tiene registros. Solo deben aparecer Eolica1 e Hidro1:
        assertEquals(Arrays.asList("Eolica1", "Hidro1"), sistema.plantasQueNuncaCumplieronCuota());
    }

    // =========================================================================
    // 6. PUNTO 10: REPORTE EÓLICO CON MÚLTIPLES PLANTAS Y BORDE EXACTO
    // =========================================================================

    @Test
    public void reporteEolicoConVelocidadExactamenteEnElLimiteYMultiplesEolicas() {
        sistema.registrarPlantaEolica("Eolica2", "Ruta 9", 500, PUESTA, "Viento", 0.5, 12, 90, 8.0);

        // En Eolica1 (mínima 5.0):
        // Hora 00: viento exactamente 5.0 -> Alcanza la mínima, NO debe aparecer en el reporte
        sistema.registrarMonitoreo("Eolica1", DIA1 + "T00:00:00", 100, 0, 5.0);
        // Hora 09: vientos de 2.0 y 4.5 -> Nunca alcanza 5.0, debe mostrar el máximo (4.5) y formato "09"
        sistema.registrarMonitoreo("Eolica1", DIA1 + "T09:00:00", 100, 0, 2.0);
        sistema.registrarMonitoreo("Eolica1", DIA2 + "T09:00:00", 100, 0, 4.5);

        // En Eolica2 (mínima 8.0):
        // Hora 15: viento de 7.5 -> Nunca alcanza 8.0, debe mostrar "15: 7.5 m/s"
        sistema.registrarMonitoreo("Eolica2", DIA1 + "T15:00:00", 100, 0, 7.5);

        String reporte = sistema.horasSinVelocidadMinimaPorPlantaEolica();
        assertFalse(reporte.contains("00:"));
        assertTrue(reporte.contains("09: 4.5 m/s"));
        assertTrue(reporte.contains("Eolica2"));
        assertTrue(reporte.contains("15: 7.5 m/s"));
    }

    // =========================================================================
    // 7. PUNTO 11: LIMPIEZA CON ITERATOR EN MÚLTIPLES PLANTAS SEGUIDAS
    // =========================================================================

    @Test
    public void limpiaMultiplesMonitoreosConsecutivosEnVariasPlantas() {
        // Dos registros vacíos seguidos en la misma planta (prueba de fuego para el Iterator)
        sistema.registrarMonitoreo("Solar1", DIA1 + "T01:00:00", 0, 0);
        sistema.registrarMonitoreo("Solar1", DIA1 + "T02:00:00", 0, 0);
        sistema.registrarMonitoreo("Solar1", DIA1 + "T03:00:00", 0, 2); // Tiene incidencias, NO se borra

        // Registros vacíos en Eólica y Térmica (con viento o CO2, pero energía 0 e incidencias 0)
        sistema.registrarMonitoreo("Eolica1", DIA1 + "T01:00:00", 0, 0, 4.0);
        sistema.registrarMonitoreo("Termica1", DIA1 + "T01:00:00", 0, 0, 10.0, 5.0);

        assertEquals(4, sistema.limpiarMonitoreosSinDatos());
        // Si volvemos a limpiar inmediatamente, debe devolver 0
        assertEquals(0, sistema.limpiarMonitoreosSinDatos());
    }

    // =========================================================================
    // 8. PUNTO 13: HISTORIAL DE PERIODOS DE INACTIVIDAD (ACTIVAR Y DESACTIVAR)
    // =========================================================================

    @Test
    public void consultaPlantasInactivasConMultiplesPeriodosCerradosYAbiertos() {
        // Solar1 se desactiva a las 10:00 y se reactiva a las 14:00 del DIA1
        sistema.desactivarPlanta("Solar1", DIA1 + "T10:00:00");
        sistema.activarPlanta("Solar1", DIA1 + "T14:00:00");

        // Eolica1 se desactiva a las 12:00 del DIA1 y queda inactiva indefinidamente
        sistema.desactivarPlanta("Eolica1", DIA1 + "T12:00:00");

        // 1. Antes de las 10:00 -> Ninguna inactiva
        assertTrue(sistema.plantasInactivasEn(DIA1 + "T09:00:00").isEmpty());

        // 2. A las 11:00 -> Solo Solar1 estaba inactiva
        assertEquals(Collections.singletonList("Solar1"), sistema.plantasInactivasEn(DIA1 + "T11:00:00"));

        // 3. A las 13:00 -> Ambas estaban inactivas (Solar1 en su periodo cerrado, Eolica1 abierta)
        assertEquals(Arrays.asList("Solar1", "Eolica1"), sistema.plantasInactivasEn(DIA1 + "T13:00:00"));

        // 4. A las 16:00 (después de reactivar Solar1) -> Solo Eolica1 sigue inactiva
        assertEquals(Collections.singletonList("Eolica1"), sistema.plantasInactivasEn(DIA1 + "T16:00:00"));
    }

    // =========================================================================
    // 9. VERIFICACIÓN DE TOSTRING Y EQUALS/HASHCODE
    // =========================================================================

    @Test
    public void verificaImplementacionDeToStringLegible() {
        String textoSistema = sistema.toString();
        assertNotNull(textoSistema);
        // Verifica que no imprima la referencia de memoria por defecto de Object (SmartGrids@...)
        assertFalse(textoSistema.contains("SmartGrid.SmartGrids@"));
        assertTrue(textoSistema.contains("Solar1"));
        assertTrue(textoSistema.contains("Termica1"));
    }
}