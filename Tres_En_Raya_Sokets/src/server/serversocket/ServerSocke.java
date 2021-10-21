package server.serversocket;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import server.singleton.Sesion;
import view.Frame;
import view.FrameEspera;

public class ServerSocke extends Thread {

    private static ServerSocke INSTANCE;
    private static FrameEspera fespera;
    private static int sesionesServerPermitidas = 1;

    public static ServerSocke getInstanceServer(FrameEspera espera) {
        fespera = espera;
        if (INSTANCE == null) {
            INSTANCE = new ServerSocke(5000);
        }
        return INSTANCE;
    }

    private int puerto;
    private ServerSocket serverSocket;
    private boolean isRun;

    private PropertyChangeSupport observed;

    public ServerSocke(int puerto) {
        this.INSTANCE = this;

        this.puerto = puerto;
        observed = new PropertyChangeSupport(this);
        System.out.println("Intentando inicar server socket en puerto: " + puerto);

        try {
            this.serverSocket = new ServerSocket(puerto);
            this.start();
            System.out.println("Server iniciado esperando conexiones...");
        } catch (Exception e) {
            System.out.println("error al iniciar socket-server");

        }

    }

    @Override
    public void run() {
        isRun = true;
        while (isRun) {
            try {
                Socket sockeNewConexion = INSTANCE.serverSocket.accept();
                fespera.dispose();
                new Frame(new SocketSesion(sockeNewConexion, observed));
            } catch (Exception e) {

            }
        }
    }

    public void addObserver(PropertyChangeListener panel) {
        observed.addPropertyChangeListener(panel);
    }
}
