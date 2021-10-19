package client.view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

    // JPanel panel = new JPanel();
    // PanelCuadricula[] paneles = new PanelCuadricula[9];

    ArrayList<PanelCuadricula> paneles = new ArrayList<PanelCuadricula>();

    public Frame() {
        this.setSize(360, 380);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("Tres en Raya");
        this.setResizable(true);
        this.setLayout(null);

        this.creaPanel();
        this.createBackground();
    }

    public void createBackground() {
        JPanel panel = new JPanel();
        panel.setBounds(10, 10, 320, 320);
        panel.setVisible(true);
        panel.setBackground(Color.BLACK);
        this.add(panel);
    }

    public void creaPanel() {
        int size = 100;
        int pxpanel;
        int pypanel = 10;

        for (int i = 0; i < 3; i++) {
            pxpanel = 10;
            for (int j = 0; j < 3; j++) {
                PanelCuadricula panel = new PanelCuadricula(i, j);
                panel.setBounds(pxpanel, pypanel, size, size);
                panel.setVisible(true);
                panel.setBackground(Color.WHITE);
                this.add(panel);
                pxpanel += size + 10;
                paneles.add(panel);
            }
            pypanel += size + 10;
        }

    }

}
