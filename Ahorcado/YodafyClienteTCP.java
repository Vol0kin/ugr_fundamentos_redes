//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class YodafyClienteTCP {

	public static void main(String[] args) {
		String mensajeServidor;
		byte []buferEnvio;
		byte []buferRecepcion = new byte[256];
		int bytesLeidos=0;

		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=8989;
		int j = 0;
		// Socket para la conexión TCP
		Socket socketServicio=null;

		try {
			// Creamos un socket que se conecte a "host" y "port":
			socketServicio = new Socket (host,port);

			PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
			BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));

			mensajeServidor = inReader.readLine();
			System.out.println(mensajeServidor);
			
			do{
				mensajeServidor = inReader.readLine();
				System.out.println(mensajeServidor);


	            Scanner input = new Scanner(System.in);
	            String inputUser = input.next();

				outPrinter.println(inputUser);

				mensajeServidor = inReader.readLine();
				System.out.println(mensajeServidor);

				j++;
			}while (j < 15);

			mensajeServidor = inReader.readLine();
			System.out.println(mensajeServidor);

			socketServicio.close();
			//////////////////////////////////////////////////////

			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
