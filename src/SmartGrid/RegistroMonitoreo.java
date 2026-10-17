package SmartGrid;

import java.time.LocalDateTime;

public class RegistroMonitoreo {
		private int idRegistro;
		private LocalDateTime fechaHora;
		private double energia;
		private int cantIncidencias;
		
		public RegistroMonitoreo(int idRegistro, LocalDateTime fechaHora, double energia, int cantIncidencias) {
			this.idRegistro = idRegistro;
			this.fechaHora = fechaHora;
			this.energia = energia;
			this.cantIncidencias = cantIncidencias;
		}

		public int getIdRegistro() {
			return idRegistro;
		}

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

		public double getEnergia() {
			return energia;
		}

		public int getCantIncidencias() {
			return cantIncidencias;
		}
		
		
}
