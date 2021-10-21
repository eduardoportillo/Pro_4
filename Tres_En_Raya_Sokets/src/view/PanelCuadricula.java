package view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class PanelCuadricula extends JPanel implements MouseListener {

    // JLabel label = new JLabel();

    private int posicionx;
    private int posicionY;
    private Frame frame;
    private String marca = "";

    public PanelCuadricula(int posicionx, int posicionY, Frame frame) {
        this.posicionx = posicionx;
        this.posicionY = posicionY;
        this.frame = frame;

        this.addMouseListener(this);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void marcar(String tipo) {
        if (!this.marca.equals("")) {
            return;
        }
        this.marca = tipo;
        this.repaint();
        this.validate();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D G2D = (Graphics2D) g;
        BasicStroke grosor = new BasicStroke(5);
        G2D.setStroke(grosor);

        if (marca.equals("")) {
            return;
        }

        if (marca.equals("O")) {
            G2D.setColor(Color.BLUE);
            G2D.drawOval(15, 15, 70, 70);
        } else {
            G2D.setColor(Color.RED);
            G2D.drawLine(20, 90, 90, 20);
            G2D.drawLine(20, 20, 90, 90);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        frame.notificar(posicionx, posicionY);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
