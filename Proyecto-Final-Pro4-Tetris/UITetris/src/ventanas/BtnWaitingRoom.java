
package ventanas;

import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.json.JSONObject;
import socketclient.SocketSession;

public class BtnWaitingRoom extends javax.swing.JPanel {
    
    private String id;
    Lobby JFPadre;
    public BtnWaitingRoom(String id, Lobby JFPadre) {
        this.JFPadre =JFPadre;
        initComponents();
        this.id = id;
    }

    public JLabel getJugadoresOnline() {
        return jugadoresOnline;
    }

    public JLabel getNombreSala() {
        return nombreSala;
    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nombreSala = new javax.swing.JLabel();
        jugadoresOnline = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));

        nombreSala.setFont(new java.awt.Font("Matura MT Script Capitals", 0, 18)); // NOI18N
        nombreSala.setForeground(new java.awt.Color(255, 255, 255));
        nombreSala.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nombreSalaMouseClicked(evt);
            }
        });

        jugadoresOnline.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jugadoresOnline.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(nombreSala, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jugadoresOnline)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreSala, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jugadoresOnline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nombreSalaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nombreSalaMouseClicked
        System.out.println("Ingresando a una sala de espera...");
        WaitingRoomChat WRC = new WaitingRoomChat(this.id);
        JSONObject sendSessinoWR = new JSONObject();
        sendSessinoWR.put("type", "SendSessionWR");
        sendSessinoWR.put("WRId", this.id);
        SocketSession.getInstance("mensaje").sendString(sendSessinoWR.toString());
        
        WRC.setVisible(true);
        JFPadre.dispose();
    }//GEN-LAST:event_nombreSalaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jugadoresOnline;
    private javax.swing.JLabel nombreSala;
    // End of variables declaration//GEN-END:variables
}
