package serversocket;

import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import serversocket.businesslogic.*;
import serversocket.server.ServerS;
import serversocket.server.SocketSession;

public class ControllerSesion {
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss");
    Date date = new Date(System.currentTimeMillis());

    public ControllerSesion(JSONObject json, SocketSession sesion, PropertyChangeSupport observed) {
        switch (json.getString("type")) {
        case "RegistrarNombre":
            sesion.setSesionName(json.getString("NombreSesion"));
            sesion.onOpen();
            break;

        case "GetWaitingRooms":
            ServerS.getInstanceServer().sendAll(Lobby.getInstance().toJson().toString());
            break;

        case "CreateWaitingRoom":
            new WaitingRoom(sesion);
            break;

        case "SendSessionWR":
            WaitingRoom waitingRoom = Lobby.getInstance().HMWaitingRoom.get(json.get("WRId"));
            waitingRoom.HMSessions.put(sesion.getKey(), sesion);
            sesion.setWaitingRoom(waitingRoom);

            JSONObject jsonGetSesionesWR = new JSONObject();
            JSONArray sessionesWR = new JSONArray();

            jsonGetSesionesWR.put("type", "GetSesionesWR");
            Lobby.getInstance().HMWaitingRoom.get(json.get("WRId")).HMSessions
                    .forEach((k, v) -> sessionesWR.put(v.toJson()));
            jsonGetSesionesWR.put("sessionesWR", sessionesWR);

            ServerS.getInstanceServer().sendAll(jsonGetSesionesWR.toString());
            break;
        case "changeReady":
            sesion.setIsReady(json.getBoolean("isReady"));
            break;
        case "StartGame":
            String idWR = json.getString("WRId");
            WaitingRoom wr = Lobby.getInstance().HMWaitingRoom.get(idWR);
            wr.startGame();
            break;
        case "completeLine":
            Game.games.get(json.getString("idGame")).completeLine(sesion);
            break;
        case "loseGame":
            Game.games.get(json.getString("idGame")).loseGame(sesion);
            break;

        case "Chat":
            switch (json.getString("mensajeType")) {
            case "text":
                WaitingRoom WRChat = Lobby.getInstance().HMWaitingRoom.get(json.get("WRid"));
                JSONObject sendMensajeText = new JSONObject();
                sendMensajeText.put("type", "Chat");
                sendMensajeText.put("mensajeType", "text");
                sendMensajeText.put("mensajeChat", json.get("mensajeChat"));
                sendMensajeText.put("nameJugador", WRChat.HMSessions.get(sesion.getKey()).getSesionName());
                sendMensajeText.put("horaEnvio", formatter.format(date));
                WRChat.HMSessions.forEach((k, v) -> v.sendString(sendMensajeText.toString()));

                break;

            case "imagen":
                WaitingRoom WRChatByImg = Lobby.getInstance().HMWaitingRoom.get(json.get("WRid"));
                json.put("nameJugador", WRChatByImg.HMSessions.get(sesion.getKey()).getSesionName());
                json.put("horaEnvio", formatter.format(date));
                // System.out.println(json.get("imgB64"));
                
                WRChatByImg.HMSessions.forEach((k, v) -> v.sendString(json.toString()));
                break;
            }
            break;

        default:
            System.out.println("");

        }

    }

}
