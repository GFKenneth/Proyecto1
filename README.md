Proyecto 1: Reconocimiento de Figuras

Contenido del repositorio: 
* figuras/: Paquete donde se encuentra el código del programa en Java.
* pruebas/: Banco de imágenes de prueba para evaluar el funcionamiento del código.
* ReporteTecnico.pdf: Reporte técnico sobre el proyecto.
* ReporteModeloLenguaje.pdf: Reporte sobre el uso de algún modelo de lenguaje.


Requisitos de ejecución:
Para compilar y ejecutar este programa se debe tener instalado Java Development Kit (JDK)* versión 8 o superior.

Instrucciones de compilación y de ejecución:
En tu terminal, posiciónate en la carpeta raíz del poryecto llamada Proyecto1 (donde se encuentra la carpeta figuras/, el reporte técnico, etc).
Para compilar, ejecuta el comando "javac figuras/*.java".
Para ejecutar el programa, se requiere de una imagen obligatoriamente.
Ejecuta la clase principal (Main) pasando como argumento la ruta del archivo de imagen .bmp que procesarás de la siguiente forma:
"java figuras.Main <ruta_imagen.bmp>" (Ejemplo con imágenes del banco de prueba: "java figuras.Main pruebas/04_figuras.bmp").
