package server.serversocket;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

import org.json.JSONObject;

import server.singleton.Sesion;
import view.Frame;

public class SocketSesion extends Thread {
    private Socket socket;
    private boolean isRun;
    private SocketSesion instance;
    private String key;
    private BufferedReader request;
    private ObjectInputStream requestObject;

    private PrintWriter response;
    private PropertyChangeSupport observed;

    private Frame frame;

    public SocketSesion(Socket socketP, PropertyChangeSupport observed) {
        this.observed = observed;
        this.instance = this;
        this.socket = socketP;
        Sesion.getListasessiones().add(this);
        String adr = socketP.getRemoteSocketAddress().toString();
        key = adr;
        this.start();
    }

    @Override
    public void run() {
        try {
            request = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = new PrintWriter(socket.getOutputStream(), true);

        } catch (Exception e1) {
            System.out.println("Error al iniciarlizar request and response");
            return;
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
                System.out.println("error en hilo socket-session");
                isRun = false;
                onClose();
            }
        }
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getKey() {
        return key;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void onClose() {

    }

    public void onOpen() {
        System.out.println("Nueva session iniciada con exito:::");
        System.out.println("IP:" + socket.getInetAddress());
        System.out.println("PORT:" + socket.getPort());
        observed.firePropertyChange("socketSession", "open", "msns");

    }

    @Override
    public String toString() {

        return "SocketSesion:  IP: " + socket.getInetAddress() + "  Port: " + socket.getPort();
    }

    public void onMensaje(String line) {
        JSONObject obj = new JSONObject(line);
        this.frame.marcar(obj);
    }

    public void send(String line) {
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
            response.println(b64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
