
package figuras;
 
import java.io.IOException;
 
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java figuras.Main <ruta_imagen.bmp>");
            return;
        }
 
        try {
            Imagen imagen = LectorBMP.leer(args[0]);
            System.out.println("Ancho: " + imagen.getAncho());
            System.out.println("Alto: " + imagen.getAlto());
            System.out.printf("Color del pixel (0,0): #%06X%n", imagen.getPixel(0, 0));
        } catch (IOException e) {
            System.out.println("Error al leer la imagen: " + e.getMessage());
        }
    }
}