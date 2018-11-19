import java.net.Socket;
import java.lang.InterruptedException;



/*
	Clase que implementa la superclase Thread
	Permite ejecutar en un thread el juego del Ahorcado
*/

public class AhorcadoThread extends Thread {
	private Ahorcado juegoAhorcado;
	private int numJugador;

	public AhorcadoThread(Socket socketServicio, int numJugador) {
		System.out.println("(201) Ahorcado ha creado una nueva partida con el cliente " + numJugador);
		juegoAhorcado = new Ahorcado(socketServicio);
		this.numJugador = numJugador;
	}

	public void run() {
		juegoAhorcado.ahorcame();
		System.out.println("(202) Ahorcado ha terminado la partida con el cliente " + numJugador);
	}



}
