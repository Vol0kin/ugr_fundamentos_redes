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
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Collections;

public class Ahorcado{
    private Socket socketServicio;
    // stream de lectura (por aquí se recibe lo que envía el cliente)
    private BufferedReader inReader;
    // stream de escritura (por aquí se envía los datos al cliente)
    private PrintWriter outPrinter;

	private String palabra;
	Random numRand = new Random();


    public Ahorcado(Socket socketServicio) {
        this.socketServicio=socketServicio;
    }

	public void ahorcame() {
		String mensaje;
		boolean salir = false;

		try {
			inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
			outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);

			outPrinter.println("Bienvenido al juego del Ahorcado. Selecciona la opcion:");

			do {
				outPrinter.println("(1) Jugar partida");
				outPrinter.println("(2) Puntuaciones");
				outPrinter.println("(3) Salir");

				mensaje = inReader.readLine();
				System.out.println("\nRecibido mensaje " + mensaje);

				String codigo = mensaje.substring(0, 5);

				switch (codigo) {
					case "(500)":
						this.jugar();
						break;
					case "(501)":
						this.mostrarPuntuaciones();
						break;
					case "(502)":
						salir = true;
						break;
					default:
						break;
				}
			} while (!salir);
		} catch (IOException e) {
			System.err.println("(904) Error al obtener los flujos de entrada/salida.");
		}
	}

    public void jugar(){
    	this.palabra = AhorcadoServer.listaPalabras.get(numRand.nextInt(AhorcadoServer.listaPalabras.size()));

		// Parseo de la palabra para eliminar acentos
        this.palabra = Normalizer
							.normalize(palabra, Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]", "");

		System.out.println("Un jugador comenzó una partida con la palabra: " + palabra);

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
		long tiempoActual = System.currentTimeMillis(), tiempoFinal = -1;

		// Duracion en segundos de una partida
		final long segundosPartida = 60;
		boolean encontrada = false,	timeout = false;
		String respuesta;

		// Obtener las letras unicas de la palabra
		for (char c : palabra.toCharArray()) {
			letrasPalabra.add(c);
		}

        try {
			/*
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
			*/

            respuesta = "(300) Palabra de " + palabra.length() + " letras. Tienes " + intentos +
            			" intentos y " + segundosPartida + " segundos.";
			outPrinter.println(respuesta);

            while (!encontrada && intentos > 0 && !timeout){
				this.mostrarPalabra(letrasAcertadas);
                respuesta = "(301) Inserta una letra:";
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
                    respuesta = "(302) La letra " + userInput +
                                " ya la has dicho, te quedan " + intentos + " intentos y " + (tiempoFinal - tiempoActual) / 1000 + " segundos";

                } else if (letrasPalabra.contains(userInput)) {
                    letrasAcertadas.add(userInput);
					letrasPalabra.remove(userInput);
					respuesta = "(303) Acertaste, te siguen quedando " + intentos + " intentos y " + (tiempoFinal - tiempoActual) / 1000 + " segundos";

                } else {
                    intentos--;
                    letrasFalladas.add(userInput);
                    respuesta = "(304) La letra " + userInput +
                                " no se encuentra en la palabra, te quedan " + intentos + " intentos y " + (tiempoFinal - tiempoActual) / 1000 + " segundos";
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
                respuesta += "(401) Número de intentos superado. La palabra era: " + palabra + ". Has perdido.";
				outPrinter.println(respuesta);
				System.out.println("Partida con la palabra " + palabra + " terminada sin ser acertada");
            } else if (timeout) {
				respuesta += "(402) Se ha agotado el tiempo: partida terminada. La palabra era: " + palabra + ". Has perdido.";
				outPrinter.println(respuesta);
				System.out.println("Partida con la palabra " + palabra + " terminada sin ser acertada");
			} else {
				float tiempoPartida = segundosPartida - (tiempoFinal - tiempoActual) / 1000;
				PrintWriter out;

				respuesta += "(400) Adivinaste la palabra en " + tiempoPartida + " segundos. Has ganado!";
	            outPrinter.println(respuesta);

				respuesta = "(403) Escribe tu nombre: ";
				outPrinter.println(respuesta);

				String nombreJugador = inReader.readLine();

				String linea = tiempoPartida + " " + nombreJugador + "->" + palabra;

				try {
					out = new PrintWriter(new BufferedWriter(new FileWriter("marcadores.txt", true)));

					System.out.println("Archivo marcadores abierto con exito. Escribiendo...");

					out.println(linea);

					System.out.println("Escritura realizada con exito. Cerrando...");

					out.close();

					System.out.println("Cerrado archivo con exito.");
				} catch (FileNotFoundException f) {
					System.out.println("No se ha podido encontrar el fichero");
				}

				System.out.println("Partida con la palabra " + palabra + " terminada.\n"+
								   "El jugador se ha identificado como " + nombreJugador);
			}
        } catch (IOException e) {
            System.err.println("(904) Error al obtener los flujos de entrada/salida.");
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

	public void mostrarPuntuaciones() {
		// Array de elementos del ranking
		// Cada posicion guarda un par clave valor
		ArrayList<Map.Entry<Float, String>> ranking = new ArrayList<Map.Entry<Float, String>>();
		String linea;
		BufferedReader br;
		File archivo = new File("marcadores.txt");

		try {
			br = new BufferedReader(new FileReader(archivo));

			System.out.println("Archivo cargado con exito. Leyendo palabras...");

			try {
				while ((linea = br.readLine()) != null) {
					String[] descomposicion = linea.split(" ");
					ranking.add(new AbstractMap.SimpleEntry<Float, String>(Float.parseFloat(descomposicion[0]), descomposicion[1]));
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

		Collections.sort(ranking, (a, b) -> a.getKey().compareTo(b.getKey()));

		int numElementos = ranking.size() <= 10 ? ranking.size() : 10;
		String mensaje = "(600) " + Integer.toString(numElementos);
		outPrinter.println(mensaje);

		for (int i = 0; i < numElementos; i++) {
			mensaje = "(601) " + (i + 1) + " " + ranking.get(i).getKey() + " " + ranking.get(i).getValue();
			outPrinter.println(mensaje);
		}
	}
}
