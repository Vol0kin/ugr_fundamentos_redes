import java.net.Socket;
import java.lang.InterruptedException;



/*
	Clase que implementa la superclase Thread
	Permite ejecutar en un thread el juego del Ahorcado
*/

public class AhorcadoThread extends Thread {
	private Ahorcado juegoAhorcado;
	private int numJugador;
	private String palabra;

	public AhorcadoThread(Socket socketServicio, String palabra, int numJugador) {
		System.out.println("Ahorcado ha creado una nueva partida.\nJugador "
							+ numJugador + "\tPalabra: " + palabra);
		juegoAhorcado = new Ahorcado(socketServicio, palabra);
		this.numJugador = numJugador;
		this.palabra = palabra;
	}

	public void run() {
		juegoAhorcado.ahorcame();
		System.out.println("Ahorcado ha terminado una partida.\nJugador "
							+ numJugador + "\tPalabra: " + palabra);
	}



}
