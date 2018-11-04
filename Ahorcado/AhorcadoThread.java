import java.net.Socket;

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
		System.out.println("Atendiento jugador " + numJugador);
		juegoAhorcado.ahorcame();
		System.out.println("Se ha atendido al jugador " + numJugador);
	}
}
