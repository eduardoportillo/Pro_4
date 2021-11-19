package socketclient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Base64;
import java.util.Enumeration;
import javax.swing.JOptionPane;

import org.json.JSONObject;

public class SocketSession extends Thread {

    private static SocketSession INSTANCE;
    public static boolean encotroServer;
    
    public static SocketSession getInstance(String ip) {
        if (ip.equals("mensaje")) {
            return INSTANCE;
        } else {
            INSTANCE = new SocketSession(ip, 5000);
        }
        return INSTANCE;
    }

    public static String buscarServer() {
        
        String ip = getLocalHostLANAddress().getHostAddress();
        String ips[] = ip.split("\\.");
        ip = "";
        for (int i = 0; i < ips.length - 1; i++) {
            ip += ips[i] + ".";
        }
        String aux = "";
        for (int i = 2; i < 256; i++) {
            if (INSTANCE == null || !INSTANCE.isRun) {
                aux = ip + i;
                System.out.print("#");
                getInstance(aux);
            } else {
                break;
            }
        }
        if(aux.equals(ip+"255")){
            JOptionPane.showMessageDialog(null, "No se Pillo Server");
            SocketSession.encotroServer=false;
        }else{
            SocketSession.encotroServer=true;
            System.out.println("El servidor se encuentra en el Host:: " + aux);
        }
        return aux;
    }

    private static InetAddress getLocalHostLANAddress() {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();

            return jdkSuppliedAddress;
        } catch (Exception e) {
            return null;
        }
    }

//////// Fin de Atributo y Metodos Staticos
    
    private boolean isRun;
    private BufferedReader request;
    private PrintWriter response;
    private String key;
    
    private PropertyChangeSupport observed;

    Socket socket;

    public SocketSession(String ip, int puerto) {
        observed = new PropertyChangeSupport(this);
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, puerto), 20);
            key = (getLocalHostLANAddress()+ ":" + socket.getLocalPort());
            isRun = true;
            this.start();
        } catch (Exception e) {
            isRun = false;
        }
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
        onOpen();
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
        System.out.println("IP SERVER:" + socket.getInetAddress());
        System.out.println("PORT SERVER: " + socket.getPort());
    }

    public void onMensaje(String line) {
        JSONObject obj = new JSONObject(line);
        new ControllerSession(obj, this, observed);
    }

    public void sendString(String line) {
        if (response == null) {
            return;
        }
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
//            observed.firePropertyChange("socketSession", "mensaje", obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return key;
    }

}
