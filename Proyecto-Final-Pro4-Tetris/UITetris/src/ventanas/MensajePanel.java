package ventanas;

import javax.swing.JLabel;

public class MensajePanel extends javax.swing.JPanel {

    public MensajePanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateMP = new javax.swing.JLabel();
        nameUserMP = new javax.swing.JLabel();
        mensajeMP = new javax.swing.JLabel();

        nameUserMP.setFont(new java.awt.Font("Matura MT Script Capitals", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 144, Short.MAX_VALUE)
                .addComponent(dateMP, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameUserMP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(mensajeMP, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(nameUserMP)
                .addGap(18, 18, 18)
                .addComponent(mensajeMP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(dateMP))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateMP;
    private javax.swing.JLabel mensajeMP;
    private javax.swing.JLabel nameUserMP;
    // End of variables declaration//GEN-END:variables

    public JLabel getDateMP() {
        return dateMP;
    }

    public void setDateMP(JLabel dateMP) {
        this.dateMP = dateMP;
    }

    public JLabel getMensajeMP() {
        return mensajeMP;
    }

    public void setMensajeMP(JLabel mensajeMP) {
        this.mensajeMP = mensajeMP;
    }

    public JLabel getNameUserMP() {
        return nameUserMP;
    }

    public void setNameUserMP(JLabel nameUserMP) {
        this.nameUserMP = nameUserMP;
    }
}
