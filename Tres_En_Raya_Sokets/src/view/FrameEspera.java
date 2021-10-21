package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class FrameEspera extends JFrame {
    JLabel mensajeEspera = new JLabel("Esperando Conexion del Cliente... ");

    public FrameEspera() {

        this.setSize(300, 150);
        // this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("Tres en Raya - Esperando Conexion");
        this.setResizable(true);
        this.setLayout(null);
        this.add(mensajeEspera);

        mensajeEspera.setBounds(40, 30, 240, 50);
        mensajeEspera.setFont(new Font("Serif", Font.BOLD, 14));
        mensajeEspera.setForeground(Color.RED);

    }
}
