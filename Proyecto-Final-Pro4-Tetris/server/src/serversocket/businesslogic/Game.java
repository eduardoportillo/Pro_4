package serversocket.businesslogic;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import serversocket.server.SocketSession;

public class Game {

    public static HashMap<String, Game> games = new HashMap<>();

    private final Double point_per_line = 1.0;

    private String id;
    public HashMap<String, SocketSession> jugadores = new HashMap<>();
    public HashMap<String, Double> puntos = new HashMap<>();
    public HashMap<String, SocketSession> looser = new HashMap<>();

    public Game(HashMap<String, SocketSession> jugadores) {
        this.jugadores = jugadores;
        this.id = UUID.randomUUID().toString();
        games.put(this.id, this);
        JSONObject startGame = new JSONObject();
        startGame.put("type", "StartGame");
        startGame.put("idGame", this.getId());
        jugadores.forEach((k, v) -> v.sendString(startGame.toString()));
    }

    public void completeLine(SocketSession sesion) {
        Double point = this.puntos.get(sesion.getKey());
        if (point == null) {
            point = 0.0;
        }
        point += this.point_per_line;
        this.puntos.put(sesion.getKey(), point);
        JSONObject objSend = new JSONObject();
        objSend.put("type", "completeLine");
        objSend.put("idGame", this.getId());
        objSend.put("idSession", jugadores.get(sesion.getKey()).getKey());
        jugadores.forEach((k, v) -> {
            v.sendString(objSend.toString());
        });
    }

    public void loseGame(SocketSession sesion) {
        this.looser.put(sesion.getKey(), sesion);

        JSONObject objSend = new JSONObject();
        objSend.put("type", "loseGame");
        objSend.put("idGame", this.getId());
        objSend.put("idSession", sesion.getKey());
        jugadores.forEach((k, v) -> {
            v.sendString(objSend.toString());
        });

        if (this.looser.size() >= this.jugadores.size() - 1) {
            JSONObject objSend2 = new JSONObject();
            objSend2.put("type", "endGame");
            objSend2.put("idGame", this.getId());
            jugadores.forEach((k, v) -> {
                if (!this.looser.containsKey(v.getKey())) {
                    objSend2.put("NameWinner", v.toJson().getString("SesionName"));
                    objSend2.put("SessionIdWinner", v.toJson().getString("SocketId"));
                }
            });
            jugadores.forEach((k, v) -> {
                v.sendString(objSend2.toString());
            });
            games.remove(this.id);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
