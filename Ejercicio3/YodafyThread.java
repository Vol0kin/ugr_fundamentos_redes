import java.net.Socket;
import java.io.IOException;

// Clase para gestionar Threads
// Guarda un ProcesadorYodafy

public class YodafyThread extends Thread {

	// ProcesadorYodafy de cada Thread
	private ProcesadorYodafy procesador;
	private Socket socketServicio;
	private int numPeticion;

	// Constructor
	// Recibe como parametro un objeto de tipo Socket
	// Inicializa el parametro procesador
	public YodafyThread(Socket socketServicio, int numPeticion) {
		this.socketServicio = socketServicio;
		this.procesador = new ProcesadorYodafy(socketServicio);
		this.numPeticion = numPeticion;
	}

	// Metodo llamado cuando se llame al metodo start()
	// Procesa una peticion
	@Override
	public void run() {
		procesador.procesa();

		System.out.println("Yoda ha terminado de procesar la peticion " + numPeticion + ". Cerrando conexion...");

		try {
			socketServicio.close();
		} catch (IOException e) {
			System.out.println("Error al cerrar el socket");
		}
	}
}
