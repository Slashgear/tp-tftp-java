package tftpclient.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Antoine
 */
public class Fmain extends javax.swing.JFrame {

    /**
     * Creates new form Fmain
     */
    public Fmain() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelMainTitre = new javax.swing.JLabel();
        jButtonTelecharger = new javax.swing.JButton();
        jButtonEnvoyer = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TFTP Client App");

        jLabelMainTitre.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabelMainTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMainTitre.setText("CLIENT TFTP");
        jLabelMainTitre.setFocusable(false);
        jLabelMainTitre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButtonTelecharger.setText("Télécharger Fichier");
        jButtonTelecharger.setName(""); // NOI18N
        jButtonTelecharger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTelechargerActionPerformed(evt);
            }
        });

        jButtonEnvoyer.setText("Envoyer Fichier");
        jButtonEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnvoyerActionPerformed(evt);
            }
        });

        jButton1.setText("Quitter");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(117, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonEnvoyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonTelecharger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelMainTitre, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jLabelMainTitre, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jButtonTelecharger)
                .addGap(18, 18, 18)
                .addComponent(jButtonEnvoyer)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(42, 42, 42))
        );

        jButtonTelecharger.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonTelechargerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTelechargerActionPerformed
        FrReceive receive = new FrReceive();
        this.getContentPane().removeAll();
        this.setContentPane(receive);
        this.pack();
        this.setVisible(true);
    }//GEN-LAST:event_jButtonTelechargerActionPerformed

    private void jButtonEnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnvoyerActionPerformed
        FrSend send = new FrSend();
        this.getContentPane().removeAll();
        this.setContentPane(send);
        this.pack();
        this.setVisible(true);
    }//GEN-LAST:event_jButtonEnvoyerActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jButton1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonEnvoyer;
    private javax.swing.JButton jButtonTelecharger;
    private javax.swing.JLabel jLabelMainTitre;
    // End of variables declaration//GEN-END:variables
}
