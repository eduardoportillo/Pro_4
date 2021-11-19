package tetris;

public class Pieza {

    private final Cuadricula[][] FormaPiezas = {
            // Pieza
            { cc(0, 4), cc(0, 5), cc(1, 4), cc(1, 5) },
            // Pieza
            { cc(1, 3), cc(1, 4), cc(1, 5), cc(1, 6) },
            // Pieza
            { cc(0, 3), cc(1, 3), cc(1, 4), cc(1, 5) },
            // Pieza
            { cc(0, 5), cc(1, 3), cc(1, 4), cc(1, 5) },
            // Pieza
            { cc(1, 3), cc(1, 4), cc(0, 4), cc(0, 5) },
            // Pieza
            { cc(0, 3), cc(0, 4), cc(1, 4), cc(1, 5) },
            // Pieza
            { cc(1, 3), cc(1, 4), cc(1, 5), cc(0, 4) } };

    // cc = CreateCuadricula
    private static Cuadricula cc(int x, int y) {
        return new Cuadricula(x, y);
    }

    public PiezaActiva getPiezaActiva(int idPieza) {
        Cuadricula[] piezaNueva = new Cuadricula[4];
        for (int i = 0; i < 4; i++) {
            piezaNueva[i] = new Cuadricula(FormaPiezas[idPieza][i].posX, FormaPiezas[idPieza][i].posY);
        }
        return new PiezaActiva(piezaNueva, idPieza + 1);
    }

    public int[] getPiezaRandom(){
        int[] res = new int[7];
        for (int i = 0; i < 7; i++) {
            res[i] = i;
        }
        randomFunction(0, res);
        return res;
    }

    private void randomFunction(int i, int[] a){
        if (i == 6) {
            return;
        }
        int intercambiar = (int) (Math.random() * (6 - i) + i + 1);
        int temp = a[i];
        a[i] = a[intercambiar];
        a[intercambiar] = temp;
        randomFunction(i+1, a);
    }

}
