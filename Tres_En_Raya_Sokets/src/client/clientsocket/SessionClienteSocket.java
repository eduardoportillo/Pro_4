package client.clientsocket;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;

import org.json.JSONObject;

import view.Frame;

public class SessionClienteSocket extends Thread {

    private static SessionClienteSocket INSTANCE;

    // InetAddress addr = InetAddress.getLocalHost();

    // private String IPLocal;

    public static SessionClienteSocket getInstance() {
        if (INSTANCE == null) {
        }
        INSTANCE = new SessionClienteSocket("127.0.0.1", 5000);
        return INSTANCE;
    }

    private boolean isRun;
    private BufferedReader request;
    private PrintWriter response;
    private Frame frame;

    private PropertyChangeSupport observed;

    Socket socket;

    public SessionClienteSocket(String ip, int puerto) {
        observed = new PropertyChangeSupport(this);
        System.out.println("Intentando conectar con el servidor socket ::: IP: " + ip + ":" + puerto);
        try {
            socket = new Socket(ip, puerto);
        } catch (Exception e) {
            System.out.println("cliente no inicia");
        }
        this.start();

    }

    public void addObserver(PropertyChangeListener panel) {
        observed.addPropertyChangeListener(panel);
    }

    @Override
    public void run() {
        try {
            request = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = new PrintWriter(socket.getOutputStream(), true);

        } catch (Exception e1) {
            System.out.println("Error al iniciarlizar request and response");
            System.exit(0);
        }
        isRun = true;
        onOpen();

        String line;
        while (isRun) {
            try {
                line = request.readLine();
                if ((line == null) || (line.equalsIgnoreCase("QUIT"))) {
                    onClose();
                    isRun = false;
                } else {
                    onMensaje(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error en hilo socket-session");
                isRun = false;
                System.exit(0);
            }
        }
    }

    public boolean isRun() {
        return isRun;
    }

    public void onClose() {
        System.out.println("Session cerrada:::" + socket.getInetAddress());
    }

    public void onOpen() {
        System.out.println("Nueva session iniciada con exito:::");
        System.out.println("IP:" + socket.getInetAddress());
        System.out.println("PORT:" + socket.getPort());
        new Frame(this);
    }

    public void onMensaje(String line) {
        JSONObject obj = new JSONObject(line);
        this.frame.marcar(obj);
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public void sendString(String line) {
        response.println(line);
        response.flush();
    }

    public void sendObject(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            String b64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            // response.println(b64);
            observed.firePropertyChange("socketSession", "mensaje", obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
