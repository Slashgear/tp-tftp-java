package tftpclient.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.SwingUtilities;
import tftpclient.Observers.TFTPObserver;
import tftpclient.TFTPReceive;
import tftpclient.TFTPSend;
import tftpclient.TFTPTransaction;

/**
 *
 * @author Antoine
 */
public class FrReceive extends javax.swing.JPanel implements ActionListener, TFTPObserver {

    private TFTPReceive receive;

    /**
     * Creates new form FrReceive
     */
    public FrReceive() {
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldIP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldNomFichier = new javax.swing.JTextField();
        jButtonTelecharger = new javax.swing.JButton();
        jButtonQuitter = new javax.swing.JButton();

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setForeground(new java.awt.Color(0, 204, 0));
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel1.setText("Adresse IP");

        jTextFieldIP.setToolTipText("Adresse IP de provenance");

        jLabel3.setText("Nom du fichier");

        jTextFieldNomFichier.setToolTipText("Nom du fichier");

        jButtonTelecharger.setText("Télécharger");
        jButtonTelecharger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTelechargerActionPerformed(evt);
            }
        });

        jButtonQuitter.setText("Quitter");
        jButtonQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jTextFieldNomFichier, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonQuitter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonTelecharger, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jTextFieldNomFichier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jButtonTelecharger)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonQuitter)
                .addGap(105, 105, 105))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonTelechargerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTelechargerActionPerformed
        if (!"".equals(jTextFieldIP.getText())) {
            if (!"".equals(jTextFieldNomFichier.getText())) {
                receive = new TFTPReceive(jTextFieldNomFichier.getText(), jTextFieldIP.getText());
                receive.addObserver(this);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jButtonQuitter.enable(false);
                        jButtonTelecharger.enable(false);
                    }
                });
                Thread monThread = new Thread(receive);
                monThread.start();
            } else {
                jTextArea1.append("Pas de nom de fichier\n");
            }
        } else {
            jTextArea1.append("Pas d'ip saisie\n");
        }
    }//GEN-LAST:event_jButtonTelechargerActionPerformed

    private void jButtonQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitterActionPerformed

        System.exit(0);
    }//GEN-LAST:event_jButtonQuitterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonQuitter;
    private javax.swing.JButton jButtonTelecharger;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldIP;
    private javax.swing.JTextField jTextFieldNomFichier;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void onErrorOccured(final String errormsg) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextArea1.append(errormsg + "\n");
            }
        });
    }

    @Override
    public void onInfoSending(final String infoMsg) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTextArea1.append(infoMsg + "\n");
            }
        });
    }

    @Override
    public void onSendingEnd(char valeur) {
    }

    @Override
    public void onReceivingEnd(char valeur) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jButtonQuitter.enable(true);
                jButtonTelecharger.enable(true);
            }
        });

    }

    @Override
    public void onProcessingSend(int value) {
    }
}
