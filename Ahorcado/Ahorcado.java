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
		System.out.println(palabra);
        this.palabra = Normalizer
							.normalize(palabra, Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]", "");
		System.out.println(this.palabra);
    }

    public void ahorcame(){
		char[] letrasEncotradas = new char[palabra.length()];

		// Creamos dos conjuntos de letras: las letras de la palabra
		//									las letras acertadas
		Set<Character> letrasPalabra = new TreeSet<>(),
					   letrasAcertadas = new TreeSet<>();
		int intentos = 10;
		boolean encontrada = false;
		String respuesta;

		// Obtener las letras unicas de la palabra
		for (char c : palabra.toCharArray()) {
			letrasPalabra.add(c);
		}

        try {
            inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);

            respuesta = "(Servidor) Palabra de " + palabra.length() + " letras. Tienes " + intentos + " intentos";
			outPrinter.println(respuesta);

            while ( !encontrada && intentos > 0 ){
				this.mostrarPalabra(letrasAcertadas);
                respuesta = "(Cliente) Inserta una letra:";
                outPrinter.println(respuesta);

				respuesta = "";

                String peticion = inReader.readLine();
                //Scanner input = new Scanner(System.in);
                char userInput = peticion.charAt(0);

                if (letrasAcertadas.contains(userInput)) {
                    intentos--;
                    respuesta = "(Servidor) La letra " + userInput +
                                 " ya la has dicho, te quedan " + intentos + " intentos";
                } else if (letrasPalabra.contains(userInput)) {
                    letrasAcertadas.add(userInput);
					letrasPalabra.remove(userInput);
                } else {
                    intentos--;
                    respuesta = "(Servidor) La letra "+ userInput +
                                 " no se encuentra en la palabra, te quedan " + intentos + " intentos";
                }

                if (letrasPalabra.isEmpty()) {
                    encontrada = true;
				}

                outPrinter.println(respuesta);
            }

            if (intentos == 0) {
                respuesta += "(Servidor) Número de intentos superado. La palabra era: " + palabra + ". Has perdido.\n";
            } else {
				respuesta += "(Servidor) Adivinaste la palabra. Has ganado!";
			}

            outPrinter.println(respuesta);
            System.out.println("meeeeh");

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
