package socketclient;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.json.JSONObject;
import ventanas.FrameTetris;
import ventanas.WaitingRoomChat;

public class ControllerSession {

    public ControllerSession(JSONObject json, SocketSession sesionServer, PropertyChangeSupport observed) {
        switch (json.getString("type")) {
        case "CreateWaitingRoom":
            observed.firePropertyChange("CreateWaitingRoom", "", json);
            break;

        case "GetWaitingRooms":
            observed.firePropertyChange("GetWaitingRooms", "", json);
            break;

        case "GetSesionesWR":
            observed.firePropertyChange("GetSesionesWR", "", json);
            break;

        case "DeleteWaitingRoom":
            observed.firePropertyChange("DeleteWaitingRoom", "", json);
            break;

        case "StartGame":
            observed.firePropertyChange("StartGame", "", json);
            FrameTetris tetris = new FrameTetris(json.getString("idGame"),
                    SocketSession.getInstance("mensaje").getKey());
            break;

        case "StartGameclock":
            observed.firePropertyChange("StartGameclock", "", json);
            break;

        case "completeLine":
            observed.firePropertyChange("completeLine", "", json);
            break;

        case "loseGame":
            observed.firePropertyChange("loseGame", "", json);
            break;

        case "endGame":
            observed.firePropertyChange("endGame", "", json);
            break;

        case "Chat":
            switch (json.getString("mensajeType")) {
            case "text":
                observed.firePropertyChange("mensajeChatText", "", json);
                break;

            case "imagen":

                try {
                    byte[] data = Base64.getDecoder().decode(json.get("imgB64").toString());
                    BufferedImage img = (BufferedImage) ImageIO.read(new ByteArrayInputStream(data));
                    ImageIcon imgIcon = new ImageIcon(img);
                    observed.firePropertyChange("mensajeChatImg", imgIcon, json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
            break;

        default:
            break;
        }
    }
}
