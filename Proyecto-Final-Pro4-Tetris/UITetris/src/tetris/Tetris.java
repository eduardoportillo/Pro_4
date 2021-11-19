package tetris;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import javax.swing.JOptionPane;
import org.json.JSONObject;
import socketclient.SocketSession;

public class Tetris extends Panel implements KeyListener, PropertyChangeListener {

    public String idGame;
    public String idSession;
    JSONObject jsonFinDelJuego;

    private int heightTablero = 22;
    private int widthTablero = 10;
    final int maxSizeheightTablero = heightTablero;

    public static boolean multiplayer = false;

    private int[][] tablero = new int[heightTablero][widthTablero];

    private BufferedImage bi;
    private Graphics graficos;
    private Dimension sizeFrame;

    private final int[] niveles = { 800, 720, 630, 550, 470, 380, 300, 220, 130, 100, 20 }; 

    private final int bloqueoGlobal = 1000;

    private final Color[] colorCuadricula = { Color.lightGray, Color.MAGENTA, Color.BLUE, Color.RED, Color.YELLOW,
            Color.ORANGE, Color.GREEN, Color.CYAN };
    private final Color colorPiezaFantasma = Color.DARK_GRAY;
    private final Color fondo = new Color(0, 0, 100);
    private final Color UIColor = Color.white;

    private Queue<Integer> cola = new ArrayDeque<Integer>();

    private Pieza pieza = new Pieza();

    private PiezaActiva piezaActual = null;

    private boolean estaEnEspera = false;

    private int tiempo = 0;
    private int retraso = niveles[0];
    private int nivel = 0;
    private int tiempoBloqueo = 0;
    private int lineasDespejadas = 0;

    private final int[] posUI = { 50, 100, 150, 200, 300 };

    private boolean juegoFinalizado = false;

    public Tetris(String idGame, String idSession) {
        this.jsonFinDelJuego = new JSONObject();
        this.idGame = idGame;
        this.idSession = idSession;

        addKeyListener(this);

        if (multiplayer == true) {
            SocketSession.getInstance("mensaje").addObserver(this);
            timer.scheduleAtFixedRate(mover, 1000, 1);
        } else {
            timer.scheduleAtFixedRate(mover, 1000, 1);
        }

    }

    private Timer timer = new Timer();
    private TimerTask mover = new TimerTask() {
        @Override
        public void run() {
            final String id_game = idGame;
            if (juegoFinalizado) {
                return;
            }

            // rellenar la cola si tiene espacio libre
            if (cola.size() < 4) {
                for (int id : pieza.getPiezaRandom()) {
                    cola.offer(id);
                }
            }

            if (tiempo >= retraso) {

                if (piezaActual == null) {
                    piezaActual = pieza.getPiezaActiva(cola.poll());
                }

                if (moverPieza(1, 0)) {
                    tiempoBloqueo = 0;
                    tiempo = 0;
                } else if (tiempoBloqueo >= bloqueoGlobal) {
                    juegoFinalizado = true;
                    for (int i = 0; i < 4; i++) {
                        tablero[piezaActual.pos[i].posX][piezaActual.pos[i].posY] = piezaActual.id;
                        if (piezaActual.pos[i].posX >= 2) {
                            juegoFinalizado = false;
                        }
                    }
                    if (juegoFinalizado) {
                        if (multiplayer == true) {
                            loseGame();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Perdiste!!!. Oprima Q Si Desea Salir o R Si Desea Reiniciar.");
                        }
                    }

                    // coloque la pieza y permita que el usuario sostenga una pieza. El tiempo de
                    // bloque también se restablece
                    synchronized (piezaActual) {
                        piezaActual = null;
                        estaEnEspera = false;
                        tiempoBloqueo = 0;
                    }

                    despejarLineas();
                    ajustarNivel(); 

                    // inmediatamente consigue otra pieza
                    tiempo = retraso;
                }
                repaint();
            }
            tiempo++;
            tiempoBloqueo++;
        }

    };

    private void despejarLineas() {
        while (true) {
            // comprobando si hay una línea que está llena
            int indice = -1;
            for (int j = 0; j < heightTablero; j++) {
                int cnt = 0;
                for (int i = 0; i < 10; i++) {
                    cnt += tablero[j][i] != 0 ? 1 : 0;
                }
                if (cnt == 10) {
                    indice = j;
                    if (multiplayer == true) {
                        sendCompleteLine();
                    }
                    break;
                }

            }

            if (indice == -1) {
                break;
            }

            // eliminando las líneas completas una por una
            int[][] temp = new int[heightTablero][10];
            for (int i = 0; i < heightTablero; i++) {
                for (int j = 0; j < 10; j++) {
                    temp[i][j] = tablero[i][j];
                }
            }
            for (int i = 0; i < indice + 1; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i == 0) {
                        tablero[i][j] = 0;
                    } else {
                        tablero[i][j] = temp[i - 1][j];
                    }
                }
            }
            lineasDespejadas++;
        }
    }

    public void sendCompleteLine() {
        JSONObject jsonSendLinea = new JSONObject();
        jsonSendLinea.put("type", "completeLine");
        jsonSendLinea.put("idGame", this.idGame);
        SocketSession.getInstance("mensaje").sendString(jsonSendLinea.toString());
    }

    protected void quitarPiso(int lineas) {
        System.out.println("entor a metodo quitarPiso()");
        for (int i = 0; i < heightTablero; i++) {
            for (int j = 0; j < widthTablero; j++) {
                if (tablero[i][j] != 0 && i - lineas < 0) {
                    loseGame();
                } else if (i - lineas >= 0) {
                    tablero[i - lineas][j] = tablero[i][j];
                }
            }
        }
        if (heightTablero > 0) {
            heightTablero--;
            System.out.println("Piso Quitado. Cantidad de Pisos Restantes: " + heightTablero);
        }
        repaint();
    }

    protected void añadirPiso(int lineas) { 
        System.out.println("entor a metodo añadirPiso()");

        if (heightTablero < maxSizeheightTablero) {
            for (int i = 0; i < heightTablero; i++) {
                for (int j = 0; j < widthTablero; j++) {
                    if (i + lineas <= maxSizeheightTablero) {
                        tablero[i + lineas][j] = tablero[i][j];
                        return;
                    }
                }
            }
            heightTablero++;
            System.out.println("Piso Añadido. Cantidad de Pisos Restantes: " + heightTablero);
            repaint();
        }
    }

    private void ajustarNivel() {
        nivel = lineasDespejadas / 4;
        if (nivel >= 9) {
            retraso = niveles[9];
        } else {
            retraso = niveles[nivel];
        }
    }

    public void paint(Graphics g) {
        sizeFrame = getSize();
        bi = new BufferedImage(sizeFrame.width, sizeFrame.height, BufferedImage.TYPE_INT_RGB);
        graficos = bi.getGraphics();
        update(g);
    }

    public void update(Graphics g) {
        graficos.setColor(fondo);
        graficos.fillRect(0, 0, sizeFrame.width, sizeFrame.height);
        mostrarTablero();
        mostrarPiezas();
        mostrarUI();
        g.drawImage(bi, 0, 0, this);

    }

    private void mostrarTablero() {
        for (int i = 2; i < heightTablero; i++) {
            for (int j = 0; j < widthTablero; j++) {
                graficos.setColor(colorCuadricula[tablero[i][j]]);
                graficos.fillRect(j * 25 + 10, i * 25, 24, 24);
            }
        }
    }

    private void mostrarPiezas() {
        if (piezaActual == null) {
            return;
        }
        synchronized (piezaActual) {
            int d = -1;
            // mostrando la pieza fantasma
            boolean isValid = true;
            while (isValid) {
                d++;
                for (Cuadricula cuadricula : piezaActual.pos) { 
                    if (cuadricula.posX + d >= heightTablero || tablero[cuadricula.posX + d][cuadricula.posY] != 0) {
                        isValid = false;
                    }
                }
            }
            d--;
            // pintando la pieza fantasma y la pieza activa
            graficos.setColor(colorPiezaFantasma);
            for (Cuadricula cuadricula : piezaActual.pos) {
                if (cuadricula.posX + d >= 2) {
                    graficos.fillRect(cuadricula.posY * 25 + 10, (cuadricula.posX + d) * 25, 24, 24);
                }
            }

            graficos.setColor(colorCuadricula[piezaActual.id]);
            for (Cuadricula cuadricula : piezaActual.pos) {
                if (cuadricula.posX >= 2) {
                    graficos.fillRect(cuadricula.posY * 25 + 10, cuadricula.posX * 25, 24, 24);
                }
            }
        }
    }

    // pinta la interfaz gráfica
    private void mostrarUI() {
        graficos.setColor(UIColor);
        graficos.drawString("LINEAS DESPEJADAS: " + lineasDespejadas, 10, 20);
        graficos.drawString("NIVEL ACTUAL: " + nivel, 10, 40);

        if (juegoFinalizado) {
            if (multiplayer == true) {
                loseGame();
            }
        }
        graficos.drawString("SIGUIENTE", 300, 50);

        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 4; j++) {
                    graficos.fillRect(j * 20 + 300, i * 20 + posUI[k], 19, 19);
                }
            }
        }

        // pinta la cola de piezas
        int i = 0;
        for (int id : cola) {
            PiezaActiva piezaSiguiente = pieza.getPiezaActiva(id);
            graficos.setColor(colorCuadricula[piezaSiguiente.id]);
            for (Cuadricula cuadricula : piezaSiguiente.pos) {
                graficos.fillRect((cuadricula.posY - 3) * 20 + 300, cuadricula.posX * 20 + posUI[i], 19, 19);
            }
            i++;
            if (i >= 4) {
                break;
            }
        }
    }

    private boolean moverPieza(int posX, int posY) {
        if (piezaActual == null) {
            return false;
        }
        for (Cuadricula cuadricula : piezaActual.pos) {
            if (cuadricula.posX + posX >= heightTablero) {
                return false;
            }
            if (cuadricula.posY + posY < 0 || cuadricula.posY + posY >= 10) {
                return false;
            }
            if (tablero[cuadricula.posX + posX][cuadricula.posY + posY] != 0) {
                return false;
            }
        }
        for (int i = 0; i < 4; i++) {
            piezaActual.pos[i].posX += posX;
            piezaActual.pos[i].posY += posY;
        }
        piezaActual.loColumna += posY;
        piezaActual.hiColumna += posY;
        piezaActual.loFila += posX;
        piezaActual.hiFila += posX;
        return true;
    }

    private void girarDerecha() {
        if (piezaActual.id == 1) {
            return;
        }
        Cuadricula[] np = new Cuadricula[4];
        for (int i = 0; i < 4; i++) {
            int nFila = piezaActual.pos[i].posY - piezaActual.loColumna + piezaActual.loFila;
            int nColumna = piezaActual.pos[i].posX - piezaActual.loFila + piezaActual.loColumna;
            np[i] = new Cuadricula(nFila, nColumna);
        }
        int loColumna = piezaActual.loColumna;
        int loFila = piezaActual.hiColumna;
        for (int i = 0; i < 4; i++) {
            np[i].posY = loFila - (np[i].posY - loColumna);
        }
        girar(np, piezaActual.estado * 2);
        repaint();

    }

    private void girar(Cuadricula[] pos, int id) {
        for (int i = 0; i < 5; i++) {
            boolean valid = true;
            int dFila = piezaActual.id == 2 ? moverFila2[id][i] : moverFila1[id][i];
            int dColumna = piezaActual.id == 2 ? moverColumna2[id][i] : moverColumna1[id][i];
            for (Cuadricula cuadricula : pos) {
                if (cuadricula.posX + dFila < 0 || cuadricula.posX + dFila >= heightTablero) {
                    valid = false;
                } else if (cuadricula.posY + dColumna < 0 || cuadricula.posY + dColumna >= 10) {
                    valid = false;
                } else if (tablero[cuadricula.posX + dFila][cuadricula.posY + dColumna] != 0) {
                    valid = false;
                }
            }
            if (valid) {
                for (int j = 0; j < 4; j++) {
                    piezaActual.pos[j].posX = pos[j].posX + dFila;
                    piezaActual.pos[j].posY = pos[j].posY + dColumna;
                }
                piezaActual.hiColumna += dColumna;
                piezaActual.loColumna += dColumna;
                piezaActual.hiFila += dFila;
                piezaActual.loFila += dFila;
                if (id % 2 == 1) {
                    piezaActual.estado = (piezaActual.estado + 3) % 4;
                } else {
                    piezaActual.estado = (piezaActual.estado + 1) % 4;
                }
                return;
            }
        }
    }

    private void loseGame() {
        if (multiplayer == true) {
            jsonFinDelJuego.put("type", "loseGame");
            jsonFinDelJuego.put("idGame", this.idGame);
            SocketSession.getInstance("mensaje").sendString(jsonFinDelJuego.toString());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            retraso = nivel >= 20 ? niveles[10] : niveles[nivel];
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_Q) {
            System.exit(0);
        }

        if (multiplayer == false) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                piezaActual = null;
                tablero = new int[heightTablero][widthTablero];
                cola.clear();
                nivel = 0;
                lineasDespejadas = 0;
                juegoFinalizado = false;
                repaint();
                return;
            }
        }

        if (piezaActual == null) {
            return;
        }

        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
            moverPieza(0, -1);
            repaint();
            break;

        case KeyEvent.VK_RIGHT:
            moverPieza(0, 1);
            repaint();
            break;

        case KeyEvent.VK_UP:
            girarDerecha();
            break;

        case KeyEvent.VK_DOWN:
            retraso = (nivel >= 20 ? niveles[10] : niveles[nivel]) / 8;
            break;

        case KeyEvent.VK_SPACE:
            tiempo = 1 << 30;
            tiempoBloqueo = 1 << 30;

        case KeyEvent.VK_CONTROL:
            tiempo = 1 << 30;
            while (moverPieza(1, 0))
                ;
            break;
        }
        repaint();
    }

    private final int[][] moverColumna1 = { { 0, -1, -1, 0, -1 }, { 0, +1, +1, 0, +1 }, { 0, +1, +1, 0, +1 },
            { 0, +1, +1, 0, +1 }, { 0, +1, +1, 0, +1 }, { 0, -1, -1, 0, -1 }, { 0, -1, -1, 0, -1 },
            { 0, -1, -1, 0, -1 } };

    private final int[][] moverFila1 = { { 0, 0, +1, 0, -2 }, { 0, 0, +1, 0, -2 }, { 0, 0, -1, 0, +2 },
            { 0, 0, -1, 0, +2 }, { 0, 0, +1, 0, -2 }, { 0, 0, +1, 0, -2 }, { 0, 0, -1, 0, +2 }, { 0, 0, -1, 0, +2 } };

    // bloque I
    private final int[][] moverColumna2 = { { 0, -2, +1, -2, +1 }, { 0, -1, +2, -1, +2 }, { 0, -1, +2, -1, +2 },
            { 0, +2, -1, +2, -1 }, { 0, +2, -1, +2, -1 }, { 0, +1, -2, +1, -2 }, { 0, +1, -2, +1, -2 },
            { 0, -2, +1, -2, +1 } };

    private final int[][] moverFila2 = { { 0, 0, 0, -1, +2 }, { 0, 0, 0, +2, -1 }, { 0, 0, 0, +2, -1 },
            { 0, 0, 0, +1, -2 }, { 0, 0, 0, +1, -2 }, { 0, 0, 0, -2, +1 }, { 0, 0, 0, -2, +1 }, { 0, 0, 0, -1, +2 } };

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        JSONObject jsonWR = (JSONObject) evt.getNewValue();

        switch (jsonWR.getString("type")) {
        case "completeLine":

            if (!jsonWR.getString("idSession").equals(this.idSession)) {
                quitarPiso(1);
            }

            if (jsonWR.getString("idSession").equals(this.idSession)) {
                añadirPiso(1);
            }
            break;

        case "loseGame":
            if (jsonWR.getString("idSession").equals(this.idSession)) {
                juegoFinalizado = true;
                JOptionPane.showMessageDialog(null,
                        "Perdiste!!! Si Deseas Saber quien Gano Espere o Oprima Q Si Desea Salir.");
            }
            break;
        case "endGame":
            juegoFinalizado = true;
            if (jsonWR.getString("SessionIdWinner").equals(this.idSession)) {
                JOptionPane.showMessageDialog(null, "Ganaste!!!!!, Muchas Felicidades. Oprima Q Para Salir.");
            } else {
                JOptionPane.showMessageDialog(null, "El Ganador es: " + jsonWR.getString("NameWinner"));
            }
            break;
        }
    }

}
