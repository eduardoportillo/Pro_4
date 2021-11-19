package serversocket.server;

import java.util.ArrayList;

public class ListaSesiones {

    private static ArrayList<SocketSession> listasessiones;

    public static ArrayList<SocketSession> getListasessiones() {
        if (listasessiones == null) {
            listasessiones = new ArrayList<SocketSession>();
        }
        return listasessiones;
    }
}
