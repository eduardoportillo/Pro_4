package ventanas;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import org.json.JSONArray;
import org.json.JSONObject;
import socketclient.Client;
import socketclient.SocketSession;

public class Lobby extends javax.swing.JFrame implements PropertyChangeListener {

    public ArrayList<BtnWaitingRoom> btnWRList = new ArrayList();
    JSONObject jsonWR;
    int posY = 30;

    public Lobby() {
        initComponents();
        this.setLocationRelativeTo(null);
        SocketSession.getInstance("mensaje").addObserver(this);
        this.setResizable(false);
        getWR();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LobbyPanel = new javax.swing.JPanel();
        createWR = new javax.swing.JButton();
        fondoLobby = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        LobbyPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane1.setViewportView(LobbyPanel);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 450, 400));

        createWR.setBackground(new java.awt.Color(153, 0, 153));
        createWR.setFont(new java.awt.Font("Matura MT Script Capitals", 1, 24)); // NOI18N
        createWR.setForeground(new java.awt.Color(255, 255, 255));
        createWR.setText("Crear Sala De Espera");
        createWR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createWRActionPerformed(evt);
            }
        });
        getContentPane().add(createWR, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 450, 40));

        fondoLobby.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondoLobby.jpg"))); // NOI18N
        getContentPane().add(fondoLobby, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -70, 490, 570));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createWRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createWRActionPerformed
        JSONObject jsonSendWR = new JSONObject();
        jsonSendWR.put("type", "CreateWaitingRoom");
        SocketSession.getInstance("mensaje").sendString(jsonSendWR.toString());
    }//GEN-LAST:event_createWRActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lobby.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Lobby().setVisible(true);
            }
        });
    }

    public void getWR() {
        JSONObject jsonGetWR = new JSONObject();
        jsonGetWR.put("type", "GetWaitingRooms");
        SocketSession.getInstance("mensaje").sendString(jsonGetWR.toString());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LobbyPanel;
    private javax.swing.JButton createWR;
    private javax.swing.JLabel fondoLobby;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private HashMap<String, BtnWaitingRoom> HMbtnWaitingRoom = new HashMap();
    @Override
    public void propertyChange(PropertyChangeEvent evt) {        
        jsonWR = (JSONObject) evt.getNewValue();
        switch (jsonWR.getString("type")) {
            case "CreateWaitingRoom":
                    BtnWaitingRoom NWR = new BtnWaitingRoom(jsonWR.getJSONObject("newWaitingRoom").getString("WRId"), this);
                    LobbyPanel.add(NWR, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, posY, -1, -1));
                    HMbtnWaitingRoom.put(jsonWR.getJSONObject("newWaitingRoom").getString("WRId"), NWR);
                    NWR.getNombreSala().setText("Sala de: " + jsonWR.getJSONObject("newWaitingRoom").getString("ownerName"));
                    posY += 60;
                    this.repaint();
                    this.validate();
                break;

            case "GetWaitingRooms":
                for (int i = 0; i < jsonWR.getJSONArray("listaWR").length(); i++) {
                    LobbyPanel.removeAll();
                    posY = 30;
                    JSONObject listaWR = jsonWR.getJSONArray("listaWR").getJSONObject(i);
                    BtnWaitingRoom BWR = new BtnWaitingRoom(listaWR.getString("WRId"), this);
                    HMbtnWaitingRoom.put(listaWR.getString("WRId"), BWR);
                    LobbyPanel.add(BWR, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, posY, -1, -1));
                    BWR.getNombreSala().setText("Sala de: " + listaWR.getString("ownerName"));
//                      BWR.getNombreSala().setText("Jugadores Online: "+listaWR.getString(""));
                    posY += 60;
                      this.repaint();
                    this.validate();
                }
                break;                 
                
                case "DeleteWaitingRoom":
                    BtnWaitingRoom objBtnWaitingRoom = HMbtnWaitingRoom.get(jsonWR.getString("WRId"));
                    LobbyPanel.remove(objBtnWaitingRoom);
                    HMbtnWaitingRoom.remove(jsonWR.getString("WRId"));
                    posY-=60;
                    this.repaint();
                    this.validate();
                    break;
            default:
                break;
        }
    }

}
