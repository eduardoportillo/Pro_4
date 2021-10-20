package view;

import javax.swing.*;
import java.awt.event.*;

public class PanelCuadricula extends JPanel implements MouseListener {

    JLabel label = new JLabel();

    private int posicionx;
    private int posicionY;
    private Frame frame;
    private String marca = "";

    public PanelCuadricula(int posicionx, int posicionY, Frame frame) {
        this.posicionx = posicionx;
        this.posicionY = posicionY;
        this.frame = frame;

        this.add(label);
        label.setBounds(20, 50, 30, 30);
        label.setText("X: " + posicionx + "\n" + " Y: " + posicionY);
        this.addMouseListener(this);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void marcar(String tipo) {
        JLabel marcado = new JLabel();
        if (!this.marca.equals("")) {
            return;
        }
        this.marca = tipo;
        marcado.setText(tipo);
        marcado.setBounds(30, 50, 30, 30);
        this.add(marcado);
        this.repaint();
        this.validate();
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
