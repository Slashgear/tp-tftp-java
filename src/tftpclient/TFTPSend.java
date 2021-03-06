package tftpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import tftpclient.PacketTypes.ACKPacket;
import tftpclient.PacketTypes.DATAPacket;
import tftpclient.PacketTypes.ERRORPacket;
import tftpclient.PacketTypes.WriteRequestPacket;

/**
 *
 * @author Antoine CARON
 */
public class TFTPSend extends TFTPTransaction {

    /**
     * Fichier choisi par l'utilisateur
     */
    private final File file;

    /**
     * Constructeur du TFTPSend prenant en paramètres le fichier choisi ainsi
     * que l'adresse du destinataire
     *
     * @param file Fichier choisi
     * @param ip Adresse IP du Destinataire
     */
    public TFTPSend(File file, String ip) {
        super();
        this.file = file;
        try {
            this.setIp((Inet4Address) InetAddress.getByName(ip));
        } catch (UnknownHostException ex) {
            System.out.println("Erreur lors du chargement de l'adresse IP");
        }

    }

    /**
     * Fonction qui envoi le fichier à un destinataire IP
     *
     * @return
     */
    public char Sendfile() {
        System.out.println("Fichier Correct");
        fireInfoSending("Fichier Correct");
        try {
            if (!this.getIp().isReachable(10000)) { // test if the Server Adress exist, and if it's reachable
                System.out.println("Host non joignable");
                fireErrorOccured("Host non joignable");
                return 1;
            } else {
                System.out.println("Adresse Correct");
                fireErrorOccured("Adresse Correct");
                if (!WRQtry()) {
                    fireErrorOccured("Pas de réponse du Serveur");
                    return 2;
                } else {
                    //Start the transmit of the file
                    System.out.println("Envoi du WRQ réussi \n\nDébut du Transfert...\n");
                    fireInfoSending("Envoi du WRQ réussi \n\nDébut du Transfert...\n");
                    if (!this.transmit()) {
                        fireInfoSending("Erreur lors de l'envoi du fichier");
                        return 3;
                    } else {
                        fireSendingEnd((char) 0);
                        System.out.println("Transfers Terminé");
                        return 0;
                    }
                }

            }
        } catch (IOException ex) {
            System.out.println("Pas de réponse du Serveur");
            fireErrorOccured("Pas de réponse du Serveur");
        }
        return 125;
    }

    /**
     * Envoi du Write request
     */
    private void sendWRQ() {
        WriteRequestPacket wrq_packet = new WriteRequestPacket(null, this.getIp(), this.getPort_dest(), file.getName());
        try {
            this.getSocket().send(wrq_packet.getDtg());
        } catch (IOException ex) {
            System.out.println("Echec sendWRQ");
            fireErrorOccured("Echec sendWRQ");
        }
    }

    /**
     * Analyse de la réception de la réponse au WRQ, cette méthode prends en
     * compte si le serveur ne répond pas
     *
     * @return true si la réponse est Oui, false sinon
     */
    private boolean receiveWRQanswer() {
        byte[] data = new byte[1024];
        DatagramPacket dtg = new DatagramPacket(data, 1024);
        try {
            this.getSocket().receive(dtg);
            System.out.println("ACK Answer:" + Arrays.toString(dtg.getData()));
            ERRORPacket er;
            if (ACKPacket.isACKPacket(dtg) || (ERRORPacket.isErrorPacket(dtg) && 6 == ERRORPacket.getErrorCode(dtg))) {
                this.setPort_dest(dtg.getPort());
                return true;
            }
        } catch (IOException ex) {
            System.out.println("Ack non reçu");
            fireErrorOccured("Ack non reçu");
        }
        return false;
    }

    /**
     * Fonction qui effectue 3 essais de Write Request
     *
     * @return true si le Write Request effectué obtient une réponse favorable
     */
    private boolean WRQtry() {
        int i = 0;
        while (i < 3) {
            sendWRQ();
            if (receiveWRQanswer()) {
                return true;
            } else {
                ++i;
            }
        }
        return false;
    }

    private boolean transmit() {
        FileInputStream fis = null;
        try {
            byte[] data = new byte[1024];
            fis = new FileInputStream(file);
            byte[] buf = new byte[512];
            int i = 1;
            while (fis.read(buf) != -1) {
                if (!transmitPacket(buf, i)) {
                    return false;
                }
                i++;
            }
            if (fis.available() >= 0) {
                data = new byte[fis.available()];
                fis.read(data);
                if (!transmitPacket(data, i)) {
                    return false;
                }
                fis.close();
                return true;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Fichier non trouvé");
            fireErrorOccured("Fichier non trouvé");
        } catch (IOException ex) {
            System.out.println("Echec lecture du fichier");
            fireErrorOccured("Echec lecture du fichier");
        }
        return false;
    }

    /**
     * Fonction qui envoi un paquet dont l'indice est passé en paramètre
     *
     * @param buf tableau de données
     * @param j numéro du paquet
     * @return true si l'envoi est réussi, false sinon
     */
    private boolean transmitPacket(byte[] buf, int j) {
        int i = 0;
        boolean packet_sent = false;
        DatagramPacket ack_dtg = new DatagramPacket(new byte[1024], 1024);

        while (i < 3 || packet_sent) {
            DATAPacket data_packet = new DATAPacket(buf, this.getIp(), this.getPort_dest(), j);
            try {
                System.out.println("DATA :" + Arrays.toString(data_packet.getData()));
                this.getSocket().send(data_packet.getDtg());
                this.getSocket().receive(ack_dtg);
                //Test if the answer is correct
                if (ACKPacket.isACKPacket(ack_dtg) && j == ACKPacket.getBlockNb(ack_dtg)) {
                    System.out.println("ACK  :" + Arrays.toString(ack_dtg.getData()));
                    packet_sent = true;
                    fireProccessingSend((int) (((float) j / (file.length() / 512)) * 100));
                    return true;
                }
            } catch (IOException ex) {
                System.out.println("Echec transfert du paquet");
                fireErrorOccured("Echec transfert du paquet");
            }
            i++;
        }
        return packet_sent;
    }

    /**
     * Lancement du Thread du SendFile
     */
    @Override
    public void run() {
        this.Sendfile();
    }

}
