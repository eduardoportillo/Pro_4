package serversocket.server;

import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

import org.json.JSONObject;

import serversocket.ControllerSesion;
import serversocket.businesslogic.WaitingRoom;

public class SocketSession extends Thread {
    private Socket socket;
    private boolean isRun;
    private SocketSession instance;
    private String key;
    private BufferedReader request;
    private WaitingRoom WRSocketSession;
    private Boolean isReady;
    private String SesionName;

    private PrintWriter response;
    private PropertyChangeSupport observed;

    private WaitingRoom waitingRoom;

    public SocketSession(Socket socketP, PropertyChangeSupport observed) {
        this.observed = observed;
        this.instance = this;
        this.socket = socketP;
        ListaSesiones.getListasessiones().add(this);
        key = socketP.getRemoteSocketAddress().toString(); // [ ] SLH
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
                // e.printStackTrace();
                isRun = false;
                onClose();
            }
        }
    }

    public void onClose() {
        if (waitingRoom != null) {
            waitingRoom.removeSesion(this);
        }
        ListaSesiones.getListasessiones().remove(this);
        System.out.println("Sesion Cerrada:: " + "Nombre Usuario: " + SesionName + " | IP CLIENT:"
                + socket.getInetAddress() + " | PORT CLIENT: " + socket.getPort());
    }

    public void onOpen() {
        System.out.println("Nueva session iniciada con exito:::");
        System.out.println("NAME_USER: " + SesionName + " | IP_CLIENT: " + socket.getInetAddress()
                + " | PORT_CLIENT: " + socket.getPort());
        observed.firePropertyChange("socketSession", "open", "msns");
    }

    @Override
    public String toString() {

        return "SocketSesion:  IP: " + socket.getInetAddress() + "  Port: " + socket.getPort();
    }

    public void onMensaje(String line) {
        JSONObject obj = new JSONObject(line);
        new ControllerSesion(obj, this, observed);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJson() {
        JSONObject jsonSS = new JSONObject();
        jsonSS.put("SocketId", instance.getKey());
        jsonSS.put("SesionName", this.getSesionName());
        jsonSS.put("isReady", this.isReady);
        return jsonSS;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getKey() {
        return key;
    }

    public String getSesionName() {
        return SesionName;
    }

    public void setSesionName(String sesionName) {
        SesionName = sesionName;
    }

    public WaitingRoom getWRSocketSession() {
        return WRSocketSession;
    }

    public void setWRSocketSession(WaitingRoom wRSocketSession) {
        WRSocketSession = wRSocketSession;
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

    public void setIsReady(Boolean isReady) {
        this.isReady = isReady;
    }

    public Boolean getIsReady() {
        return isReady;
    }
}
