package client.view;

import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuFrame extends JFrame {
    JButton btnServer = new JButton("Quiero ser servidor");
    JButton btnClient = new JButton("Quiero ser Cliente");

    public MenuFrame() {
        this.setSize(270, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("Tres en Raya");
        this.setResizable(true);
        this.setLayout(null);
        this.add(btnServer);
        this.add(btnClient);

        btnServer.setVisible(true);
        btnServer.setBounds(30, 60, 200, 100);

        btnClient.setVisible(true);
        btnClient.setBounds(30, 200, 200, 100);

        actionBtnServer();
        actionBtnClient();

    }

    private void actionBtnServer() {
        btnServer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "esperando conexion");
            }

        });
    }

    private void actionBtnClient() {
        btnClient.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "esperando conexion");
            }

        });
    }

}
