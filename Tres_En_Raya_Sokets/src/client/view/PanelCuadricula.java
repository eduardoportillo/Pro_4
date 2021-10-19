package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelCuadricula extends JPanel implements MouseListener {

    JLabel label = new JLabel();

    private int posicionx;
    private int posicionY;

    public PanelCuadricula(int posicionx, int posicionY) {
        this.posicionx = posicionx;
        this.posicionY = posicionY;

        this.add(label);
        label.setBounds(20, 50, 30, 30);
        label.setText("X: " + posicionx + "\n" + " Y: " + posicionY);
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // JOptionPane.showMessageDialog(null, "X: " + posicionx + "\n" + " Y: " +
        // posicionY);
        JLabel marcado = new JLabel();
        marcado.setText("marcado");
        marcado.setBounds(30, 50, 30, 30);
        this.add(marcado);
        this.repaint();
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
