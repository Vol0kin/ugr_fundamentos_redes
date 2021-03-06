import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class AhorcadoServer {

	static ArrayList<String> listaPalabras = new ArrayList<String>();

	public static void main(String[] args) {

		// Puerto de escucha
		int port=8989;

		// Socket del servidor
		ServerSocket serverSocket = null;

		File archivo = new File("palabras.txt");
		BufferedReader br = null;
		String linea;
		int numClientes = 0;

		System.out.println("Cargando archivo del que leer...");

		// Creacion de un buffer de lectura sobre el archivo y lectura de palabras
		try {
			br = new BufferedReader(new FileReader(archivo));

			System.out.println("Archivo cargado con exito. Leyendo palabras...");

			try {
				while ((linea = br.readLine()) != null) {
					listaPalabras.add(linea);
				}
			} catch (IOException e) {
				System.out.println("Error al leer linea del archivo");
			}

			System.out.println("Palabras leidas con exito. Cerrando archivo...");

			try {
				br.close();
			} catch (IOException e) {
				System.out.println("Error al cerrar el fichero");
			}

			System.out.println("Cerrado archivo con exito.");
		} catch (FileNotFoundException f) {
			System.out.println("No se ha podido encontrar el fichero");
		}

		try {
			// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
			System.out.println("(101) Abriendo el socket del servidor en modo pasivo...");

			serverSocket = new ServerSocket(port);

			System.out.println("(102) Abierto socket en modo pasivo con exito");
		} catch (IOException e) {
			System.err.println("(901) Error al escuchar en el puerto "+port);
		}

		// Mientras ... siempre!
		do {
			
			Socket socketServicio = null;

			System.out.println("(103) Esperando sockets...");
			// Aceptamos una nueva conexión con accept()
			/////////////////////////////////////////////////

			try {
				socketServicio = serverSocket.accept();
				numClientes++;

				System.out.println("(104) Recibido socket cliente");

				// Creamos un objeto de la clase ProcesadorYodafy, pasándole como
				// argumento el nuevo socket, para que realice el procesamiento
				// Este esquema permite que se puedan usar hebras más fácilmente.

				AhorcadoThread ahorcadoThread = new AhorcadoThread(socketServicio, numClientes);
				ahorcadoThread.start();

			} catch (IOException e) {
				System.err.println("(902) Error: no se pudo aceptar la conexion solicitada");
			}

		} while (true);

	}

}
