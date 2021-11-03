package tetris;

public class PiezaActiva {
    Cuadricula[] pos;
        int id;
        int loFila, hiFila, loColumna, hiColumna;
        int estado = 0;

        PiezaActiva(Cuadricula[] pos, int id) {
            this.pos = pos;
            this.id = id;
            if (id != 2) {
                loFila = 0;
                hiFila = 2;
                loColumna = 3;
                hiColumna = 5;
            } else {
                loFila = 0;
                hiFila = 3;
                loColumna = 3;
                hiColumna = 6;
            }
        }
}
