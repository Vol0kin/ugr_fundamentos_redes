import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativo {

	public static void main(String[] args) {

		// Puerto de escucha
		int port=8989;
		int numPeticiones = 0;

		// Socket del servidor
		ServerSocket serverSocket;

		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			//////////////////////////////////////////////////
			// ...serverSocket=... (completar)
			System.out.println("Abriendo el socket del servidor en modo pasivo...");

			serverSocket = new ServerSocket(port);

			System.out.println("Abierto socket en modo pasivo con exito");
			//////////////////////////////////////////////////

			// Mientras ... siempre!
			do {
				Socket socketServicio = null;

				System.out.println("Esperando sockets...");
				// Aceptamos una nueva conexión con accept()
				/////////////////////////////////////////////////
				// socketServicio=... (completar)
				//////////////////////////////////////////////////

				try {
					socketServicio = serverSocket.accept();
					numPeticiones++;

					System.out.println("Recibido socket cliente " + numPeticiones);

					System.out.println("Yoda se prepara para procesar la peticion...");

					// Se crea un nuevo thread para la peticion que ha llegado
					YodafyThread yodaThread = new YodafyThread(socketServicio, numPeticiones);

					// Se inicializa el thread
					yodaThread.start();
				} catch (IOException e) {
					System.err.println("Error: no se pudo aceptar la conexion solicitada");
				}

			} while (true);

		} catch (IOException e) {
			System.err.println("Error al escuchar en el puerto "+port);
		}

	}

}
