package SmartGrid;

import java.time.LocalDateTime;

public class RegistroMonitoreo {
		private LocalDateTime fechaHora;
		private double energia;
		private int cantIncidencias;
		
		public RegistroMonitoreo(LocalDateTime fechaHora, double energia, int cantIncidencias) {
			if(energia<0 || cantIncidencias <0) {
				throw new RuntimeException ("Falla en el registro. Las entradas no pueden ser negativas.");
			}
			
			this.fechaHora = fechaHora;
			this.energia = energia;
			this.cantIncidencias = cantIncidencias;
		}

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

		public double getEnergia() {
			return energia;
		}

		public int getIncidencias() {
			return cantIncidencias;
		}
		
		@Override
		public String toString() {
		    return "[" + fechaHora + " -> Energia: " + energia + ", Incidencias: " + cantIncidencias + "]";
		}
}
