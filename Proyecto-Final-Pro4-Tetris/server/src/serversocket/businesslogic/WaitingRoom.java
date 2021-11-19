package serversocket.businesslogic;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import serversocket.server.ServerS;
import serversocket.server.SocketSession;

public class WaitingRoom {

    public HashMap<String, SocketSession> HMSessions = new HashMap<>();

    private String id;
    private Date fecha;
    private SocketSession sesion;

    public WaitingRoom(SocketSession sesion) {
        if (sesion.getWaitingRoom() != null)
            return;
        this.id = UUID.randomUUID().toString();
        this.fecha = new Date();
        this.sesion = sesion;
        HMSessions.put(sesion.getKey(), sesion);
        Lobby.getInstance().addWR(id, this);
        sesion.setWaitingRoom(this);
    }

    public void removeSesion(SocketSession sesion) {
        HMSessions.remove(sesion.getKey());
        if (HMSessions.size() <= 0) {
            Lobby.getInstance().HMWaitingRoom.remove(this.getId());
            JSONObject deleteWaitingRoom = new JSONObject();
            deleteWaitingRoom.put("type", "DeleteWaitingRoom");
            deleteWaitingRoom.put("WRId", this.getId());
            ServerS.getInstanceServer().sendAll(deleteWaitingRoom.toString());
        } else {
            JSONObject jsonGetSesionesWR = new JSONObject();
            JSONArray sessionesWR = new JSONArray();
            jsonGetSesionesWR.put("type", "GetSesionesWR");
            HMSessions.forEach((k, v) -> sessionesWR.put(v.toJson()));
            jsonGetSesionesWR.put("sessionesWR", sessionesWR);
            HMSessions.forEach((k, v) -> v.sendString(jsonGetSesionesWR.toString()));
        }
    }

    public JSONObject toJson() {

        JSONObject WRAtribut = new JSONObject();

        WRAtribut.put("WRId", this.getId());
        WRAtribut.put("ownerName", sesion.getSesionName());
        WRAtribut.put("fecha", this.getFecha());

        JSONArray listaSession = new JSONArray();

        HMSessions.forEach((k, v) -> listaSession.put(v.toJson()));

        WRAtribut.put("listaSession", listaSession);
        return WRAtribut;
    }

    public void startGame() {

        JSONObject StartGameclock = new JSONObject();
        StartGameclock.put("type", "StartGameclock");
        ServerS.getInstanceServer().sendAll(StartGameclock.toString());

        Lobby.getInstance().HMWaitingRoom.remove(this.getId());
        JSONObject deleteWaitingRoom = new JSONObject();
        deleteWaitingRoom.put("type", "DeleteWaitingRoom");
        deleteWaitingRoom.put("WRId", this.getId());
        ServerS.getInstanceServer().sendAll(deleteWaitingRoom.toString());

        new Game(HMSessions);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
