import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Random;

public class Ahorcado{
    private Socket socketServicio;
    // stream de lectura (por aquí se recibe lo que envía el cliente)
    private InputStream inputStream;
    // stream de escritura (por aquí se envía los datos al cliente)
    private OutputStream outputStream;

    // Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
    private Random random;
    private static String respuesta;

    public Ahorcado(Socket socketServicio) {
        this.socketServicio=socketServicio;
        random=new Random();
    }

    public void ahorcame(){    
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> palabras = new ArrayList<String>();
        byte [] datosRecibidos=new byte[1024];
        byte [] datosEnviar;

        try {
            archivo = new File ("palabras.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while( (linea=br.readLine()) != null )
                palabras.add( linea );
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        


        try {
            String palabra = palabras.get( (int) (Math.random() * palabras.size()) );
            char[] letrasEncotradas = new char[palabra.length()];
            int cont = 0, intentos = 10, bytesRecibidos = 0;
            boolean encontrada = false;
            inputStream=socketServicio.getInputStream();
            outputStream=socketServicio.getOutputStream();

            respuesta = "(Servidor) Palabra de " + palabra.length() + " letras. Tienes " + intentos + " intentos";

            while ( !encontrada && intentos > 0 ){

                respuesta += "\n(Cliente) Inserta una letra > ";

                datosEnviar=respuesta.getBytes();
                outputStream.write(datosEnviar, 0, datosEnviar.length);
                respuesta = "";

                bytesRecibidos = inputStream.read(datosRecibidos);
                String peticion = new String(datosRecibidos,0,bytesRecibidos);
                //Scanner input = new Scanner(System.in);
                char userInput = peticion.charAt(0);


                if ( contiene(letrasEncotradas, userInput, cont) ){
                    intentos--;
                    respuesta += "(Servidor) La letra " + userInput + 
                                 " ya la has dicho, te quedan " + intentos + " intentos";
                }

                else if ( palabra.contains(String.valueOf(userInput)) ){
                    letrasEncotradas[cont] = userInput;
                    cont++;
                }

                else{
                    intentos--;
                    respuesta += "(Servidor) La letra "+ userInput + 
                                 " no se encuentra en la palabra, te quedan " + intentos + " intentos";
                }

                if ( !hayHuecos(palabra,letrasEncotradas) )
                    encontrada = true;
                
                datosEnviar=respuesta.getBytes();
                outputStream.write(datosEnviar, 0, datosEnviar.length);
                respuesta = "";
            } 

            if ( intentos==0 )
                respuesta += "\n(Servidor) Número de intentos superado. La palabra era: " + palabra + ". Has perdido.\n";
            else
                respuesta += "\n(Servidor) Adivinaste la palabra. Has ganado!";
               

            datosEnviar=respuesta.getBytes();
            outputStream.write(datosEnviar, 0, datosEnviar.length);
            outputStream.flush();
            System.out.println("meeeeh");

        } catch (IOException e) {
            System.err.println("Error al obtener los flujso de entrada/salida.");
        }
    }


    public static boolean hayHuecos(String palabra, char[] letrasEncotradas) {
        boolean huecoEncontrado = false;

        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);

            if ( new String(letrasEncotradas).contains(String.valueOf(letra)) )
                respuesta += letra;
            else {
                respuesta += '_';
                huecoEncontrado = true;
            }
        }
        return huecoEncontrado;
    }

    public static boolean contiene(char[] letrasEncotradas, char letra, int cont){
        boolean encontrada = false;
        for (int i = 0; i < cont && encontrada == false; i++)
            if (letrasEncotradas[i] == letra) encontrada = true;
        return encontrada;
    }

}