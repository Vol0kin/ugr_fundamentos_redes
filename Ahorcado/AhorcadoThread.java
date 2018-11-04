import java.net.Socket;
import java.lang.InterruptedException;



/*
	Clase que implementa la superclase Thread
	Permite ejecutar en un thread el juego del Ahorcado
*/

public class AhorcadoThread extends Thread {
	private Ahorcado juegoAhorcado;
	private int numJugador;
	boolean timeout = true;
	Thread thread1, thread2;

	public AhorcadoThread(Socket socketServicio, String palabra, int numJugador) {
		juegoAhorcado = new Ahorcado(socketServicio, palabra);
		this.numJugador = numJugador;
	}

	public void run() {
		System.out.println("Atendiento jugador " + numJugador);
		thread1 = new Thread () {
			public void run () {
				try{
					thread1.sleep(4000);
				} catch (InterruptedException e) {
					System.err.println("No se ha iniciado la hebra");
				}
				if (timeout){
					System.err.println("Tiempo agotado");
					System.exit(0);
				}
			}
		};
		thread2 = new Thread () {
			public void run () {
				juegoAhorcado.ahorcame();
				timeout = false;
				System.out.println("Se ha atendido al jugador " + numJugador);
			}
		};
		thread1.start();
		thread2.start();
	}



}
