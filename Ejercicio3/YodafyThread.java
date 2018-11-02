import java.net.Socket;

// Clase para gestionar Threads
// Guarda un ProcesadorYodafy

public class YodafyThread extends Thread {

	// ProcesadorYodafy de cada Thread
	private ProcesadorYodafy procesador;
	private int numPeticion;

	// Constructor
	// Recibe el numero de peticion
	// Inicializa un nuevo procesador
	public YodafyThread(Socket socketServicio, int numPeticion) {
		this.procesador = new ProcesadorYodafy(socketServicio);
		this.numPeticion = numPeticion;
	}

	// Metodo llamado cuando se llame al metodo start()
	// Procesa una peticion
	@Override
	public void run() {
		procesador.procesa();

		System.out.println("Yoda ha terminado de procesar la peticion " + numPeticion + ". Cerrando conexion...");
	}
}
