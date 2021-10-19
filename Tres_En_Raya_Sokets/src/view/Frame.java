package view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
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

    private String turno = "server";

    public void init() {
        this.setSize(360, 380);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(true);
        this.setLayout(null);

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
    }

    public Frame(SocketSesion server) {
        this.server = server;
        this.server.setFrame(this);
        init();
        this.setTitle("Tres en Raya - Server");
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
        panel.marcar(obj.getString("tipo"));
        cambiarTurno();
    }

    public void notificar(int x, int y) {
        JSONObject obj = new JSONObject();
        obj.put("x", x);
        obj.put("y", y);
        if (this.cliente == null) {
            // soy server
            if (!this.turno.equals("server")) {
                return;
            }
            obj.put("tipo", "server");

            this.server.send(obj.toString());
        } else {
            // soy cliente
            if (!this.turno.equals("cliente")) {
                return;
            }
            obj.put("tipo", "cliente");
            this.cliente.send(obj.toString());
        }
        PanelCuadricula panel = this.paneles[x][y];
        panel.marcar(obj.getString("tipo"));
        cambiarTurno();
    }

    public void cambiarTurno() {
        if (this.turno.equals("server")) {
            this.turno = "cliente";
        } else {
            this.turno = "server";
        }
    }

}
