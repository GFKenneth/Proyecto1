package figuras;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LectorBMP {
    //Tamano header estandar BMP
    public static final int TAMANO_HEADER = 54;

    public static Imagen leer(String ruta) throws IOException {
        try (RandomAccessFile archivo = new RandomAccessFile(ruta, "r")) {
            byte[] header = new byte[TAMANO_HEADER];
            archivo.readFully(header);
            validarFirma(header);

            int offsetDatos = leerEnteroLittleEndian(header, 10);
            int ancho = leerEnteroLittleEndian(header, 18);
            int altoHeader = leerEnteroLittleEndian(header, 22);
            short bitsPorPixel = leerCortoLittleEndian(header, 28);
            int compresion = leerEnteroLittleEndian(header, 30);
            validarFormatoSoportado(bitsPorPixel, compresion);
 
            boolean arribaHaciaAbajo = altoHeader < 0;
            int alto = Math.abs(altoHeader);
 
            int tamanoFila = ancho * 3 + calcularRelleno(ancho);
 
            byte[] datos = new byte[tamanoFila * alto];
            archivo.seek(offsetDatos);
            archivo.readFully(datos);
 
            int[][] pixeles = new int[alto][ancho];
            for (int filaArchivo = 0; filaArchivo < alto; filaArchivo++) {
                int fila = arribaHaciaAbajo ? filaArchivo : alto - 1 - filaArchivo;
                int inicioFila = filaArchivo * tamanoFila;
                for (int columna = 0; columna < ancho; columna++) {
                    int i = inicioFila + columna * 3;
                    int azul = datos[i] & 0xFF;
                    int verde = datos[i + 1] & 0xFF;
                    int rojo = datos[i + 2] & 0xFF;
                    pixeles[fila][columna] = (rojo << 16) | (verde << 8) | azul;
                }
            }
 
            return new Imagen(ancho, alto, pixeles);
        }
    }

    private static void validarFirma(byte[] header) throws IOException {
        if (header[0] != 'B' || header[1] != 'M') {
            throw new IOException("El archivo no es un BMP valido");
        }
    }

    private static short leerCortoLittleEndian(byte[] datos, int offset) {
        return (short) ((datos[offset] & 0xFF) | ((datos[offset + 1] & 0xFF) << 8));
    }

    private static void validarFormatoSoportado(short bitsPorPixel, int compresion) throws IOException {
        if (bitsPorPixel != 24) {
            throw new IOException("Solo se soportan archivos con 24 bits por pixel");
        }

        if (compresion != 0) {
            throw new IOException("Solo se soportan BMP sin compresion");
        }
    }

    private static int leerEnteroLittleEndian(byte[] datos, int offset) {
        return (datos[offset] & 0xFF)
             | ((datos[offset + 1] & 0xFF) << 8)
             | ((datos[offset + 2] & 0xFF) << 16)
             | ((datos[offset + 3] & 0xFF) << 24);
    }
 
    private static int calcularRelleno(int ancho) {
        return (4 - (ancho * 3) % 4) % 4;
    }
    
}