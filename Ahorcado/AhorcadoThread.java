import java.net.Socket;
import java.lang.InterruptedException;



/*
	Clase que implementa la superclase Thread
	Permite ejecutar en un thread el juego del Ahorcado
*/

public class AhorcadoThread extends Thread {
	private Ahorcado juegoAhorcado;
	private int numJugador;

	public AhorcadoThread(Socket socketServicio, String palabra, int numJugador) {
		juegoAhorcado = new Ahorcado(socketServicio, palabra);
		this.numJugador = numJugador;
	}

	public void run() {
		juegoAhorcado.ahorcame();
	}



}
