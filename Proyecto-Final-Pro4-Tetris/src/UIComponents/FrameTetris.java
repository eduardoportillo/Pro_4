package UIComponents;

import java.awt.Frame;

import tetris.Tetris;

public class FrameTetris extends Frame {

    private Tetris tetris;

    public FrameTetris() {
        setSize(400, 600);
        setResizable(true);
        // this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setUndecorated(true);
        setLocationRelativeTo(null);

        tetris = new Tetris();
        add(tetris);

        setVisible(true);
    }
}
