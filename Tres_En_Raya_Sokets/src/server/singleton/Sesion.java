package server.singleton;

import java.util.ArrayList;

import server.serversocket.SocketSesion;

public class Sesion {

    private static ArrayList<SocketSesion> listasessiones;

    public static ArrayList<SocketSesion> getListasessiones() {
        if (listasessiones == null) {
            listasessiones = new ArrayList<SocketSesion>();
        }
        return listasessiones;
    }
}
