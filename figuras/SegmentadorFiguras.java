package figuras;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase para segmentar la imagen en figuras usando BFS.
 * 
 * Resumiento, asumiremos que el pixel (0,0) tiene el color de fondo.
 * Hacemos un recorrido de la matriz y si se encuentra un pixel no visitado distinto del fondo, entonces usamos BFS para agarrar a los pixeles vecinos del mismo color.
 * Cada BFS que se hace crea una "FiguraDetectada".
 */

public class SegmentadorFiguras {

    // Vecinos en las direcciones de arriba, abajo, izquierda y derecha.
    private static final int[] DELTA_FILA = {-1, 1, 0, 0};
    private static final int[] DELTA_COLUMNA = {0, 0, -1, 1};

    /**
     * Recibe la imagen y devuelve la lista de figuras encontradas.
     */
    public ArrayList<FiguraDetectada> segmentar(Imagen imagen){
        int alto = imagen.getAlto();
        int ancho = imagen.getAncho();
        int colorFondo = imagen.getPixel(0, 0);

        boolean[][] visitados = new boolean[alto][ancho];
        ArrayList<FiguraDetectada> figuras = new ArrayList<FiguraDetectada>();

        for (int fila = 0; fila < alto; fila++){
            for (int columna = 0; columna < ancho; columna++){
                boolean esFondo = imagen.getPixel(fila, columna) == colorFondo;

                if (!visitados[fila][columna] && !esFondo){
                    FiguraDetectada figura = explorarFigura(imagen, visitados, fila, columna, colorFondo);
                    figuras.add(figura);
                }
            }
        }

        return figuras;
    }

    /**
     * BFS desde (filaInicio, columnaInicio) que agrupa los pixeles vecinos del mismo color y diferencía a los de contorno.
     */
    private FiguraDetectada explorarFigura(Imagen imagen, boolean[][] visitados, int filaInicio, int columnaInicio, int colorFondo){
        int colorFigura = imagen.getPixel(filaInicio, columnaInicio);
        FiguraDetectada figura = new FiguraDetectada(colorFigura);

        Queue<Punto> cola = new LinkedList<Punto>();
        cola.add(new Punto(filaInicio, columnaInicio));
        visitados[filaInicio][columnaInicio] = true;

        while (!cola.isEmpty()){
            Punto actual = cola.poll();
            figura.agregarPunto(actual);

            if (esContorno(imagen, actual, colorFondo)){
                figura.agregarPuntoContorno(actual);
            }

            // Revisa los 4 vecinos
            for (int i = 0; i < 4; i++){
                int nuevaFila = actual.getFila() + DELTA_FILA[i];
                int nuevaColumna = actual.getColumna() + DELTA_COLUMNA[i];

                if (estaDentro(imagen, nuevaFila, nuevaColumna) && !visitados[nuevaFila][nuevaColumna] && imagen.getPixel(nuevaFila, nuevaColumna) == colorFigura){
                    visitados[nuevaFila][nuevaColumna] = true;
                    cola.add(new Punto(nuevaFila, nuevaColumna));
                }
            }
        }

        return figura;
    }

    /**
     * Método para analizar si un pixel es de contorno.
     * Es de contorno si tiene al menos un vecino que sea del color del fondo.
     * También lo es si está en el borde de la imagen.
     */
    private boolean esContorno(Imagen imagen, Punto p, int colorFondo){
        for (int i = 0; i < 4; i++){
            int vecinoFila = p.getFila() + DELTA_FILA[i];
            int vecinoColumna = p.getColumna() + DELTA_COLUMNA[i];

            if (!estaDentro(imagen, vecinoFila, vecinoColumna)){
                return true;
            }
            if (imagen.getPixel(vecinoFila, vecinoColumna) == colorFondo){
                return true;
            }
        }
        return false;
    }

    /** 
     * Método para verificar que (fila, columna) dentro de la imagen.
    */
    private boolean estaDentro(Imagen imagen, int fila, int columna){
        return fila >= 0 && fila < imagen.getAlto() && columna >= 0 && columna < imagen.getAncho();
    }
}
