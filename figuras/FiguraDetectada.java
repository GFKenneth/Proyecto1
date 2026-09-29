package figuras;

import java.util.ArrayList;

/**
 * Clase para guardar la información de una figura encontrada en la imagen.
 * Guarda también sus atributos de color, sus pixeles y su contorno de pixeles.
 */
public class FiguraDetectada {
    private final int color;                       // 0xRRGGBB
    private final ArrayList<Punto> puntos;         // todos los pixeles de la figura
    private final ArrayList<Punto> contorno;       // pixeles del borde exterior

    public FiguraDetectada(int color){
        this.color = color;
        this.puntos = new ArrayList<Punto>();
        this.contorno = new ArrayList<Punto>();
    }

    public void agregarPunto(Punto p){
        puntos.add(p);
    }

    public void agregarPuntoContorno(Punto p){
        contorno.add(p);
    }

    public int getColor(){
        return color;
    }

    public ArrayList<Punto> getPuntos(){
        return puntos;
    }

    public ArrayList<Punto> getContorno(){
        return contorno;
    }

    public int getArea(){
        return puntos.size();
    }

    public String getColorHex() {
        return String.format("#%06X", color);
    }
}
