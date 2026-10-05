package figuras;

import java.util.ArrayList;

public class Clasificador {
    private static final double UMBRAL_CIRCULARIDAD = 0.97; // Circulo ~1.0, cuadrado/rombo ~0.8

    public TipoFigura clasificar(FiguraDetectada figura) {
        ArrayList<Punto> hull = cierreConvexo(figura.getContorno());
        ArrayList<Punto> esquinas = filtrarColineales(hull);
        double perimetro = calcularPerimetro(hull);
        double circularidad = calcularCircularidad(figura.getArea(), perimetro);

        if (circularidad >= UMBRAL_CIRCULARIDAD) {
            return TipoFigura.O;
        }

        switch (esquinas.size()) {
            case 3:
                return TipoFigura.T;
            case 4:
                return TipoFigura.C;
            default:
                return TipoFigura.X;
        }

    }

    //Juntar vertices falsos que salen de un borde diagonal pixelado
    private ArrayList<Punto> filtrarColineales(ArrayList<Punto> hull){
        if (hull.size() <= 2) return hull;

        //Quitar puntos casi rectos
        ArrayList<Punto> resultado = new ArrayList<Punto>();
        int n = hull.size();

        for (int i = 0; i < n; i++){
            Punto anterior = hull.get((i - 1 + n) % n);
            Punto actual = hull.get(i);
            Punto siguiente = hull.get((i + 1) % n);
            
            double d1x = actual.getColumna() - anterior.getColumna();
            double d1y = actual.getFila() - anterior.getFila();
            double d2x = siguiente.getColumna() - actual.getColumna();
            double d2y = siguiente.getFila() - actual.getFila();
            
            double mag1 = Math.hypot(d1x, d1y);
            double mag2 = Math.hypot(d2x, d2y);
            
            if (mag1 > 0 && mag2 > 0){
                //cos del angulo entre ambos segmentos
                double productoPunto = (d1x * d2x + d1y * d2y) / (mag1 * mag2);
                if (productoPunto < 0.98){
                    resultado.add(actual);
                }
            } else{
                resultado.add(actual);
            }
        }
        
        //Junta puntos muy cercanos entre si
        ArrayList<Punto> esquinasFinales = new ArrayList<Punto>();
        int m = resultado.size();
        for (int i = 0; i < m; i++){
            Punto p = resultado.get(i);
            boolean duplicado = false;
            for (Punto f : esquinasFinales){
                if (Math.hypot(p.getFila() - f.getFila(), p.getColumna() - f.getColumna()) < 8.0){
                    duplicado = true;
                    break;
                }
            }
            if (!duplicado){
                esquinasFinales.add(p);
            }
        }
        return esquinasFinales;
    }

    public ArrayList<Punto> cierreConvexo(ArrayList<Punto> puntos) {
        ArrayList<Punto> ordenados = new ArrayList<Punto>(puntos);
        ordenados.sort((p1, p2) -> {
            if (p1.getFila() != p2.getFila()) {
                return Integer.compare(p1.getFila(), p2.getFila());
            }
            return Integer.compare(p1.getColumna(), p2.getColumna());
        });

        ArrayList<Punto> inferior = new ArrayList<Punto>();
        for (Punto p : ordenados) {
            while (inferior.size() >= 2 && cruz(inferior.get(inferior.size() - 2), inferior.get(inferior.size() - 1), p) <= 0) {
                inferior.remove(inferior.size() - 1);
            }
            inferior.add(p);
        }

        ArrayList<Punto> superior = new ArrayList<Punto>();
        for (int i = ordenados.size() - 1; i >= 0; i--) {
            Punto p = ordenados.get(i);
            while (superior.size() >= 2 && cruz(superior.get(superior.size() - 2), superior.get(superior.size() - 1), p) <= 0) {
                superior.remove(superior.size() - 1);
            }
            superior.add(p);
        }

        inferior.remove(inferior.size() - 1);
        superior.remove(superior.size() - 1);
        inferior.addAll(superior);
        return inferior;
    }

    // Positivo si o->a->b gira a la izquierda, negativo si gira a la derecha
    private double cruz(Punto o, Punto a, Punto b) {
        return (double) (a.getColumna() - o.getColumna()) * (b.getFila() - o.getFila())
        - (double) (a.getFila() - o.getFila()) * (b.getColumna() - o.getColumna());
    }

    private double calcularPerimetro(ArrayList<Punto> hull) {
        double perimetro = 0;
        int n = hull.size();
        for (int i = 0; i < n; i++) {
            Punto actual = hull.get(i);
            Punto siguiente = hull.get((i + 1) % n);
            int deltaFila = siguiente.getFila() - actual.getFila();
            int deltaColumna = siguiente.getColumna() - actual.getColumna();
            perimetro += Math.hypot(deltaFila, deltaColumna);
        }
        return perimetro;
    }

    private double calcularCircularidad(int area, double perimetro) {
        return 4 * Math.PI * area / (perimetro * perimetro);
    }
}