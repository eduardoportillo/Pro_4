package serversocket.businesslogic;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import serversocket.server.ServerS;

public class Lobby {
    private static Lobby instance;
    public static Lobby getInstance(){
        if(instance==null){
            instance = new Lobby();
        }
        return instance;
    }

    public HashMap<String,WaitingRoom> HMWaitingRoom;

    public Lobby(){
        this.HMWaitingRoom = new HashMap<>();
    }

    public JSONObject toJson(){
        JSONArray listaWRs = new JSONArray();

        JSONObject wrList = new JSONObject();
        wrList.put("type", "GetWaitingRooms");

        HMWaitingRoom.forEach((k,v) ->  listaWRs.put(v.toJson()));

        wrList.put("listaWR",listaWRs);
        return wrList;
    }

    public void addWR(String id, WaitingRoom watingRoom){
        this.HMWaitingRoom.put(id, watingRoom);

        JSONObject newWR = new JSONObject();
        newWR.put("type", "CreateWaitingRoom");
        newWR.put("newWaitingRoom", watingRoom.toJson());
        ServerS.getInstanceServer().sendAll(newWR.toString());
    }
    
}
