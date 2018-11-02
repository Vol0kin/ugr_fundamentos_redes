import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Ahorcado{
    public static void main(String[] args) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<String> palabras = new ArrayList<String>();

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
        

        String palabra = palabras.get( (int) (Math.random() * palabras.size()) );
        char[] letrasEncotradas = new char[palabra.length()];
        int cont = 0, intentos = 10;
        boolean encontrada = false;

        System.out.println("(Servidor) Palabra de " + palabra.length() + " letras. Tienes " + intentos + " intentos");

        while ( !encontrada && intentos > 0 ){

            System.out.print("\n(Servidor) Inserta una letra > ");
            Scanner input = new Scanner(System.in);
            char userInput = input.nextLine().charAt(0);


            if ( contiene(letrasEncotradas, userInput, cont) ){
                intentos--;
                System.out.println("(Servidor) La letra " + userInput + 
                                   " ya la has dicho, te quedan " + intentos + " intentos");
            }

            else if ( palabra.contains(String.valueOf(userInput)) ){
                letrasEncotradas[cont] = userInput;
                cont++;
            }

            else{
                intentos--;
                System.out.println("(Servidor) La letra "+ userInput + 
                                   " no se encuentra en la palabra, te quedan " + intentos + " intentos");
            }

            if ( !hayHuecos(palabra,letrasEncotradas) )
                encontrada = true;
        } 

        if ( intentos==0 )
            System.out.println("\n(Servidor) Número de intentos superado. La palabra era: " + palabra + ". Has perdido.\n");
        else
            System.out.println("\n(Servidor) Adivinaste la palabra. Has ganado!");
    }


    public static boolean hayHuecos(String palabra, char[] letrasEncotradas) {
        boolean huecoEncontrado = false;

        for (int i = 0; i < palabra.length(); i++) {
            char letra = palabra.charAt(i);

            if ( new String(letrasEncotradas).contains(String.valueOf(letra)) )
                System.out.print(letra);
            else {
                System.out.print('_');
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