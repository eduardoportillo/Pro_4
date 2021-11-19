package ventanas;

import java.awt.Frame;

import tetris.Tetris;

public class FrameTetris extends Frame {

    private Tetris tetris;
    
    public FrameTetris(String idGame, String idSession) {
        
        this.setTitle("Sesion del Jugador: "+idSession);
        this.setResizable(false);
        setSize(400, 600);
        setResizable(true);
        setLocationRelativeTo(null);

        tetris = new Tetris(idGame, idSession);
        add(tetris);

        setVisible(true);
    }
}
