import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class AhorcadoClient {

	public static void main(String[] args) {
		String mensajeServidor, eleccionMenu;
		byte []buferEnvio;
		byte []buferRecepcion = new byte[256];
		int bytesLeidos=0;
		boolean terminado = false;

		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=8989;
		// Socket para la conexión TCP
		Socket socketServicio=null;

		try {
			// Creamos un socket que se conecte a "host" y "port":
			socketServicio = new Socket (host,port);

			PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));


			// Mensaje de bienvenida
			mensajeServidor = inReader.readLine();
			System.out.println(mensajeServidor);

			do{
				// Menu
				mensajeServidor = inReader.readLine();
				mensajeServidor += "\n" + inReader.readLine();
				mensajeServidor += "\n" + inReader.readLine();
				System.out.println(mensajeServidor);

				Scanner inputMenu = new Scanner(System.in);
				eleccionMenu = inputMenu.next();


				switch (eleccionMenu){
					// Jugar
					case "1":
						outPrinter.println("(500)");
						jugar(outPrinter, inReader, mensajeServidor);
						break;

					// Puntuaciones
					case "2":
						outPrinter.println("(501)");
						mostrarRanking(inReader);
						break;

					// Salir
					case "3":
						outPrinter.println("(502)");
						System.out.println("Hasta pronto!");
						terminado = true;
						break;

					// Elección incorrecta
					default:
						System.out.println("Por favor, elige una opción que esté en el menú");
						outPrinter.println("(503)");
						break;
				}

			} while(!terminado);

			socketServicio.close();
			/////////////////////////////////////////////////////

			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("(905) Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("(906) Error de entrada/salida al abrir el socket.");
		}
	}

	public static void jugar(PrintWriter outPrinter, BufferedReader inReader, String mensajeServidor) throws IOException{
		boolean terminado = false;

		mensajeServidor = inReader.readLine();
		System.out.println(mensajeServidor);

		do {
			mensajeServidor = inReader.readLine();
			System.out.println(mensajeServidor);

			if (!mensajeServidor.contains("_")) {
				terminado = true;
			} else {
				mensajeServidor = inReader.readLine();
				System.out.println(mensajeServidor);

		        Scanner input = new Scanner(System.in);
		        String inputUser = input.next();

				outPrinter.println(inputUser.toLowerCase());

				mensajeServidor = inReader.readLine();
				System.out.println(mensajeServidor);
			}

		} while (!terminado);

		if (mensajeServidor.contains("(400)")) {
			mensajeServidor = inReader.readLine();
			System.out.println(mensajeServidor);

			Scanner inputNombre = new Scanner(System.in);
			String nombreJugador = inputNombre.next();

			outPrinter.println(nombreJugador);
		}
	}

	public static void mostrarRanking(BufferedReader inReader) throws IOException {
		String mensaje = inReader.readLine();

		int numElementos = Integer.parseInt(mensaje.substring(6));

		for (int i = 0; i < numElementos; i++) {
			mensaje = inReader.readLine();
			System.out.println(mensaje);
		}
	}

}
