import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.text.Normalizer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Ahorcado{
    private Socket socketServicio;
    // stream de lectura (por aquí se recibe lo que envía el cliente)
    private BufferedReader inReader;
    // stream de escritura (por aquí se envía los datos al cliente)
    private PrintWriter outPrinter;

	private String palabra;

    public Ahorcado(Socket socketServicio, String palabra) {
        this.socketServicio=socketServicio;

		// Parseo de la palabra para eliminar acentos
        this.palabra = Normalizer
							.normalize(palabra, Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]", "");
    }

    public void ahorcame(){
		char[] letrasEncotradas = new char[palabra.length()];

		// Creamos dos conjuntos de letras: las letras de la palabra
		//									las letras insertadas
		Set<Character> letrasPalabra = new TreeSet<>(),
					   letrasAcertadas = new TreeSet<>(),
					   letrasFalladas = new TreeSet<>();
		int intentos = 10;

		// Variables para controlar el tiempo de partida
		// Se considera que empieza la partida cuando el cliente
		// envia el primer caracter
		long tiempoActual, tiempoFinal = -1;

		// Duracion en segundos de una partida
		final long segundosPartida = 60;
		boolean encontrada = false,	timeout = false;
		String respuesta;

		// Obtener las letras unicas de la palabra
		for (char c : palabra.toCharArray()) {
			letrasPalabra.add(c);
		}

        try {
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);

            respuesta = "Palabra de " + palabra.length() + " letras. Tienes " + intentos + " intentos";
			outPrinter.println(respuesta);

            while (!encontrada && intentos > 0 && !timeout){
				this.mostrarPalabra(letrasAcertadas);
                respuesta = "Inserta una letra:";
                outPrinter.println(respuesta);

				respuesta = "";

                String peticion = inReader.readLine();
                char userInput = peticion.charAt(0);

				tiempoActual = System.currentTimeMillis();

				if (tiempoFinal == -1) {
					tiempoFinal = tiempoActual + segundosPartida * 1000;
				}

                if (letrasAcertadas.contains(userInput)
					|| letrasFalladas.contains(userInput)) {
                    respuesta = "La letra " + userInput +
                                 " ya la has dicho, te quedan " + intentos + " intentos";

                } else if (letrasPalabra.contains(userInput)) {
                    letrasAcertadas.add(userInput);
					letrasPalabra.remove(userInput);
					respuesta = "Acertaste, te siguen quedando " + intentos + " intentos";

                } else {
                    intentos--;
                    letrasFalladas.add(userInput);
                    respuesta = "La letra " + userInput +
                                 " no se encuentra en la palabra, te quedan " + intentos + " intentos";
                }

                if (letrasPalabra.isEmpty()) {
                    encontrada = true;
				}

				if (tiempoActual > tiempoFinal) {
					timeout = true;
				}

                outPrinter.println(respuesta);
            }

			respuesta = "";

            if (intentos == 0) {
                respuesta += "Número de intentos superado. La palabra era: " + palabra + ". Has perdido.";
            } else if (timeout) {
				respuesta += "Timeout: partida terminada. La palabra era: " + palabra + ". Has perdido.";
			} else {
				respuesta += "Adivinaste la palabra. Has ganado!";
			}

            outPrinter.println(respuesta);

        } catch (IOException e) {
            System.err.println("Error al obtener los flujos de entrada/salida.");
        }
    }

	private void mostrarPalabra(Set<Character> acertadas) {
		String palabraEnviada = "";

		for (char c : palabra.toCharArray()) {
			if (acertadas.contains(c)) {
				palabraEnviada += c + " ";
			} else {
				palabraEnviada += "_ ";
			}
		}

		outPrinter.println(palabraEnviada);
	}
}
