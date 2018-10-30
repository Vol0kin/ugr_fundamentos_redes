import java.net.Socket;

// Clase para gestionar Threads
// Guarda un ProcesadorYodafy

public class YodaThread extends Thread {

	// ProcesadorYodafy de cada Thread
	ProcesadorYodafy procesador;

	// Constructor
	// Recibe como parametro un objeto de tipo Socket
	// Inicializa el parametro procesador
	public YodaThread(Socket socketServicio) {
		this.procesador = new ProcesadorYodafy(socketServicio);
	}

	// Metodo llamado cuando se llame al metodo start()
	// Procesa una peticion
	public void run() {
		procesador.procesa();
	}
}
