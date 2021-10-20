package view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONObject;

import client.clientsocket.SessionClienteSocket;
import netscape.javascript.JSObject;
import server.serversocket.SocketSesion;

public class Frame extends JFrame {

    // JPanel panel = new JPanel();
    // PanelCuadricula[] paneles = new PanelCuadricula[9];

    // ArrayList<PanelCuadricula> paneles = new ArrayList<PanelCuadricula>();
    PanelCuadricula[][] paneles = new PanelCuadricula[3][3];

    JLabel turnoLabel = new JLabel();

    private String turno = "server";

    public void init() {
        this.setSize(360, 410);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(true);
        this.setLayout(null);

        this.add(turnoLabel);
        turnoLabel.setVisible(true);
        turnoLabel.setBounds(5, 5, 400, 20);

        this.creaPanel();
        this.createBackground();
    }

    private SessionClienteSocket cliente;
    private SocketSesion server;

    public Frame(SessionClienteSocket cliente) {
        this.cliente = cliente;
        this.cliente.setFrame(this);
        init();
        this.setTitle("Tres en Raya - cliente");
        turnoLabel.setText("no es tu turno. Esperando al oponente...");
    }

    public Frame(SocketSesion server) {
        this.server = server;
        this.server.setFrame(this);
        init();
        this.setTitle("Tres en Raya - Server");
        turnoLabel.setText("es tu turno");
    }

    public void createBackground() {
        JPanel panel = new JPanel();
        panel.setBounds(10, 30, 320, 320);
        panel.setVisible(true);
        panel.setBackground(Color.BLACK);
        this.add(panel);
    }

    public void creaPanel() {
        int size = 100;
        int pxpanel;
        int pypanel = 30;

        for (int i = 0; i < 3; i++) {
            pxpanel = 10;
            for (int j = 0; j < 3; j++) {
                PanelCuadricula panel = new PanelCuadricula(i, j, this);
                panel.setBounds(pxpanel, pypanel, size, size);
                panel.setVisible(true);
                panel.setBackground(Color.WHITE);
                this.add(panel);
                pxpanel += size + 10;
                this.paneles[i][j] = panel;
            }
            pypanel += size + 10;
        }

    }

    public void marcar(JSONObject obj) {
        int i = obj.getInt("x");
        int j = obj.getInt("y");
        PanelCuadricula panel = this.paneles[i][j];
        panel.marcar(obj.getString("marca"));
        cambiarTurno();
    }

    public void notificar(int x, int y) {

        JSONObject obj = new JSONObject();
        PanelCuadricula panel = this.paneles[x][y];
        if (!panel.getMarca().equals("")) {
            return;
        }
        obj.put("x", x);
        obj.put("y", y);

        if (this.cliente == null) {
            // soy server
            if (!this.turno.equals("server")) {
                return;
            }

            obj.put("marca", "X");
            // turnoLabel.setText("es turno del server");
            this.repaint();
            this.validate();

            this.server.send(obj.toString());
        } else {
            // soy cliente
            if (!this.turno.equals("cliente")) {
                return;
            }
            obj.put("marca", "O");

            this.repaint();
            this.validate();

            this.cliente.send(obj.toString());
        }

        panel.marcar(obj.getString("marca"));
        panel.setMarca(obj.getString("marca"));

        if (gano(paneles, paneles[x][y].getMarca())) {
            JOptionPane.showMessageDialog(null, "gana " + paneles[x][y].getMarca());
        }

        cambiarTurno();
    }

    public void cambiarTurno() {
        if (this.turno.equals("server")) {
            this.turno = "cliente";
            if (this.cliente != null) {
                turnoLabel.setText("es tu turno");
            } else {
                turnoLabel.setText("no es tu turno. Esperando al oponente...");
            }
        } else {
            this.turno = "server";
            if (this.server != null) {
                turnoLabel.setText("es tu turno");
            } else {
                turnoLabel.setText("no es tu turno. Esperando al oponente...");
            }

        }
    }

    public boolean gano(PanelCuadricula matriz[][], String marca) {

        for (int i = 0; i < matriz.length; i++) {
            byte count = 0;
            for (int j = 0; j < matriz.length; j++)
                count += (matriz[i][j].getMarca().equals(marca)) ? 1 : 0;
            if (count == 3)
                return true;
        }

        for (int j = 0; j < matriz.length; j++) {
            byte count = 0;
            for (int i = 0; i < matriz.length; i++)
                count += (matriz[i][j].getMarca().equals(marca)) ? 1 : 0;
            if (count == 3)
                return true;
        }

        if (matriz[0][0].getMarca().equals(marca) && matriz[1][1].getMarca().equals(marca)
                && matriz[2][2].getMarca().equals(marca))
            return true;

        if (matriz[0][2].getMarca().equals(marca) && matriz[1][1].getMarca().equals(marca)
                && matriz[2][0].getMarca().equals(marca))
            return true;

        return false;
    }

}
