package socketclient;

import javax.swing.JProgressBar;
import org.json.JSONObject;
import ventanas.IndexFrame;

public class Client {

    private static String nameClienteSesion;
    private String serverIp;

    public Client() {
        serverIp = SocketSession.buscarServer();
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setNameClienteSesion(String nameClienteSesion) {
        this.nameClienteSesion = nameClienteSesion;
        JSONObject jsonSetNombre = new JSONObject();
        jsonSetNombre.put("type", "RegistrarNombre");
        jsonSetNombre.put("NombreSesion", nameClienteSesion);
        SocketSession.getInstance("mensaje").sendString(jsonSetNombre.toString());
    }
    
    public static String getNameClienteSesion() {
        return nameClienteSesion;
    }

    public String getServerIp() {
        return serverIp;
    }

    
}
