package figuras;

import java.util.ArrayList;

public class Clasificador {
    private static final double UMBRAL_CIRCULARIDAD = 0.9;

    public TipoFigura clasificar(FiguraDetectada figura) {
        ArrayList<Punto> hull = cierreConvexo(figura.getContorno());
        double perimetro = calcularPerimetro(hull);
        double circularidad = calcularCircularidad(figura.getArea(), perimetro);

        if (circularidad >= UMBRAL_CIRCULARIDAD) {
            return TipoFigura.O;
        }

        switch (hull.size()) {
            case 3:
                return TipoFigura.T;
            case 4:
                return TipoFigura.C;
            default:
                return TipoFigura.X;
        }

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