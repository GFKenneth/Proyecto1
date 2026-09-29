package figuras;

import java.io.IOException;
import java.util.ArrayList;

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

            SegmentadorFiguras segmentador = new SegmentadorFiguras();
            ArrayList<FiguraDetectada> figuras = segmentador.segmentar(imagen);

            System.out.println("Figuras encontradas: " + figuras.size());
            for (int i = 0; i < figuras.size(); i++) {
                FiguraDetectada f = figuras.get(i);
                System.out.println("Figura " + (i + 1)
                        + " | color: " + f.getColorHex()
                        + " | area (pixeles): " + f.getArea()
                        + " | pixeles de contorno: " + f.getContorno().size());
            }
        } catch (IOException e) {
            System.out.println("Error al leer la imagen: " + e.getMessage());
        }
    }
}