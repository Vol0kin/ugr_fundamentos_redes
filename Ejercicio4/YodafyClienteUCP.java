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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress; 

public class YodafyClienteUCP {

	public static void main(String[] args) {
		
		int puerto=8989;
		InetAddress direccion;
		DatagramPacket paquete, paqueteRecepcion;
		byte []buferEnvio = new byte[256];
		byte []buferRecepcion = new byte[256];
		DatagramSocket socket;
		String fraseYodificada;
		
		try {
			// Creamos un socket que se conecte a "host" y "port":
			//////////////////////////////////////////////////////
			socket = new DatagramSocket();
			direccion = InetAddress.getByName("localhost");
			//////////////////////////////////////////////////////			
			
			// Si queremos enviar una cadena de caracteres por un OutputStream, hay que pasarla primero
			// a un array de bytes:
			buferEnvio = "Al monte del volcán debes ir sin demora".getBytes();
			
			// Enviamos el array por el outputStream;
			//////////////////////////////////////////////////////
			paquete = new DatagramPacket(buferEnvio, buferEnvio.length, direccion, puerto);
			fraseYodificada = new String(paquete.getData());
			socket.send(paquete);
			System.out.println("Paquete enviado");
			//////////////////////////////////////////////////////
			
			// Aunque le indiquemos a TCP que queremos enviar varios arrays de bytes, sólo
			// los enviará efectivamente cuando considere que tiene suficientes datos que enviar...
			// Podemos usar "flush()" para obligar a TCP a que no espere para hacer el envío:
			//////////////////////////////////////////////////////
			//outputStream.flush();
			//////////////////////////////////////////////////////
			
			// Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que intentará
			// rellenar. El método "read(...)" devolverá el número de bytes leídos.
			//////////////////////////////////////////////////////
			paqueteRecepcion = new DatagramPacket(buferRecepcion, buferRecepcion.length);
			socket.receive(paqueteRecepcion);
			fraseYodificada = new String(paqueteRecepcion.getData());
			System.out.println("Contenido del paquete recibido: " + fraseYodificada );
			//////////////////////////////////////////////////////
			
			
			// Una vez terminado el servicio, cerramos el socket (automáticamente se cierran
			// el inpuStream  y el outputStream)
			//////////////////////////////////////////////////////
			socket.close();
			//////////////////////////////////////////////////////
			
			// Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}
