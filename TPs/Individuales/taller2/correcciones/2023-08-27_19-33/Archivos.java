package aed;

import java.io.PrintStream;
import java.util.Scanner;

class Archivos {
    float[] leerVector(Scanner entrada, int largo) {
        float[] vector = new float[largo];
        
        for (int i = 0; i < largo; i++) {
            vector[i] = entrada.nextFloat();
        }
        
        return vector;
    }

    float[][] leerMatriz(Scanner entrada, int filas, int columnas) {
        float[][] matriz = new float[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            matriz[i] = leerVector(entrada, columnas);
        }
        
        return matriz;
    }

    void imprimirPiramide(PrintStream salida, int alto) {
        int lineSize = 2 * alto - 1;
        for (int i = 0; i < alto; i++) {
            int floorSize = 1 + 2*i;
            int bordersSize = (lineSize - floorSize) / 2;

            for (int j = 0; j < bordersSize; j++) {
                salida.print(' ');
            }

            for (int j = 0; j < floorSize; j++) {
                salida.print('*');
            }

            for (int j = 0; j < bordersSize; j++) {
                salida.print(' ');
            }

            salida.println();
        }
    }
}
