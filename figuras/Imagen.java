package figuras;
 
public class Imagen {
    private final int ancho;
    private final int alto;
    private final int[][] pixeles; // pixeles[fila][columna] = 0xRRGGBB
 
    public Imagen(int ancho, int alto, int[][] pixeles) {
        this.ancho = ancho;
        this.alto = alto;
        this.pixeles = pixeles;
    }
 
    public int getAncho() {
        return ancho;
    }
 
    public int getAlto() {
        return alto;
    }
 
    public int getPixel(int fila, int columna) {
        return pixeles[fila][columna];
    }
 
    public int[][] getPixeles() {
        return pixeles;
    }
}