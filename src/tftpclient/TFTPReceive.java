package tftpclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import tftpclient.PacketTypes.ACKPacket;
import tftpclient.PacketTypes.DATAPacket;
import tftpclient.PacketTypes.ERRORPacket;
import tftpclient.PacketTypes.ReadRequestPacket;

/**
 *
 * @author Antoine
 */
public class TFTPReceive extends TFTPTransaction {

    private String directory_name;
    private static final int DEFAULT_PACKET_SIZE = 516;
    protected String filename;

    public TFTPReceive(String filename, String ip) {
        super();
        directory_name = "..\\ZonedeDépot\\";
        this.filename = filename;
        try {
            this.setIp((Inet4Address) Inet4Address.getByName(ip));
        } catch (UnknownHostException ex) {
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public char Receivefile() {
        char crrv;
        try {
            if (!this.getIp().isReachable(5000)) { // Test si l'adresse distante existe
                fireErrorOccured("Hote Non Joignable, Adresse Incorrect");
                crrv = 1;
                fireReceivingEnd(crrv);
                return crrv;
            } else {
                fireInfoSending("Hote Joignable");
                System.out.println("Hote Joignable");
                DATAPacket dtg = RRQtry(); // Demande de lescture du Fichier demandé
                if (dtg == null) { //Si le paquet reçu est une erreur ou si il n' y a pas eu de réponse
                    fireErrorOccured("Erreur lors du Request: Fin exécution");
                    System.out.println("Erreur lors du Request: Fin exécution");
                    crrv = 2;
                    fireReceivingEnd(crrv);
                    return crrv;
                } else {
                    fireInfoSending("DEBUT Receive");
                    if (transmit(dtg)) { //Lancement du receive par l'ACk du premier packet reçu
                        fireInfoSending("FIN Receive");
                        crrv = 0;
                        fireReceivingEnd(crrv);
                        return crrv;
                    } else {
                        // En cas d'erreur lors du téléchargement
                        fireInfoSending("FIN Receive : Téléchargement avorté");
                        crrv = 3;
                        fireReceivingEnd(crrv);
                        return crrv;
                    }
                }
            }
        } catch (IOException ex) {
            fireErrorOccured("Adress non joignable");
            System.out.println("Adress non joignable");
            return 125;
        }
    }

    private void sendRRQ() {
        ReadRequestPacket rrq_packet = new ReadRequestPacket(null, this.getIp(), this.getPort_dest(), filename);
        try {
            this.getSocket().send(rrq_packet.getDtg());
        } catch (IOException ex) {
            System.out.println("Echec sendRRQ");
            fireErrorOccured("Echec sendRRQ");
        }
    }

    private DATAPacket receiveRRQanswer() {
        byte[] data = new byte[DEFAULT_PACKET_SIZE];
        DatagramPacket dtg = new DatagramPacket(data, data.length);
        try {
            //écoute de la réponse du serveur
            this.getSocket().receive(dtg);
            if (DATAPacket.isDataPacket(dtg)) {
                //Si le serveur accepte le RRQ, il répond directement avec le premier DATAPacket
                this.setPort_dest(dtg.getPort());
                byte[] truncate = new byte[dtg.getLength() - 4];
                System.arraycopy(dtg.getData(), 4, truncate, 0, dtg.getLength() - 4);
                return new DATAPacket(truncate, dtg.getAddress(), dtg.getPort(), 1);
            }
            if (ERRORPacket.isErrorPacket(dtg) && (1 != ERRORPacket.getErrorCode(dtg) || 2 != ERRORPacket.getErrorCode(dtg))) {
                //Si le serveur ne trouve pas le fichier, ou si le client n'a pas les droits d'accès.
                System.out.println(ERRORPacket.getErrMsg(dtg));
                fireErrorOccured("Erreur lors du téléchargement: " + ERRORPacket.getErrMsg(dtg));
                return null;
            }
        } catch (IOException ex) {
            //Si aucun ACK n'est reçu
            System.out.println("Ack non reçu");
            fireErrorOccured("Ack non reçu");
            return new DATAPacket(new byte[0], this.getIp(), this.getPort_dest(), this.getPort_dest());
        }
        return null;
    }

    private DATAPacket RRQtry() {
        int i = 0;
        DATAPacket dtg;
        while (i < 3) { // On réalise 3 envoi de RRQ
            sendRRQ();
            dtg = receiveRRQanswer();
            if (dtg == null) {
                //Si le serveur répond non
                return null;
            } else {
                if (dtg.getData().length > 0) {
                    //Si la réponse reçu correspond à des données
                    return dtg;
                }
            }
            //itère si le seveur ne répond pas
            i++;
        }
        return null;
    }

    private boolean transmit(DATAPacket dtg) {
        FileOutputStream fos = null;
        try {
            int i = 1;
            File fi = new File(directory_name); //Création de la Zone de dépot si elle n'existe pas
            if (!fi.isDirectory()) {
                fi.mkdir();
            }
            File fichier = new File(directory_name + filename);
            DatagramPacket _dtg = new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
            fichier.createNewFile();
            fos = new FileOutputStream(fichier);
            System.out.println("DATA :" + Arrays.toString(dtg.getDtg().getData()));
            //On écrit le premier DATAPacket dans le fichier
            fos.write(dtg.getData());

            //Réponse de l'ACk de ce premier paquet
            ACKPacket ack = new ACKPacket(this.getIp(), dtg.getDtg().getPort(), ACKPacket.getBlockNb(dtg.getDtg()));
            this.getSocket().send(ack.getDtg());
            System.out.println("ACK  :" + Arrays.toString(ack.getDtg().getData()));

            if (dtg.getDtg().getLength() < 512) {
                return true;
            }
            while (true) {
                //On écoute le paquet suivant
                this.getSocket().receive(_dtg);
                if (ERRORPacket.isErrorPacket(_dtg)) {
                    //Si le serveur répond à tout moment par un paquet d'erreur on arrete le transfert 
                    System.out.println("Une Erreur est survenue: " + ERRORPacket.getErrMsg(_dtg));
                    fireErrorOccured("Une Erreur est survenue: " + ERRORPacket.getErrMsg(_dtg));
                    fos.close();
                    //Suppression du fichier crée
                    fichier.delete();
                    return false;
                } else {

                    System.out.println("DATA :" + Arrays.toString(_dtg.getData()));
                    byte[] truncate = new byte[_dtg.getLength() - 4];
                    System.arraycopy(_dtg.getData(), 4, truncate, 0, _dtg.getLength() - 4);
                    //écriture des données
                    fos.write(truncate);
                    ack = new ACKPacket(this.getIp(), dtg.getDtg().getPort(), DATAPacket.getBlockNb(_dtg));
                    System.out.println("ACK  :" + Arrays.toString(ack.getDtg().getData()));
                    //réponse de l'ACK
                    this.getSocket().send(ack.getDtg());
                    i++;
                    //On s'arrete si le paquet reçu a une taille inférieur à 512.
                    if (_dtg.getLength() < 512) {
                        break;
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            //Si on le peut pas ouvrir le fichier
            fireErrorOccured("Fichier non-ouvrable");
            System.out.println("Fichier non-ouvrable");
            return false;
        } catch (IOException ex) {
            //Si le serveur ne répond plus
            fireErrorOccured("Délai d'attente dépassé");
            System.out.println("Délai d'attente dépassé");
            return false;
        } finally {
            try {
                fos.close();
                return true;
            } catch (IOException ex) {
                System.out.println("Fermeture du fichier impossible");
                fireErrorOccured("Fermeture du fichier impossible");
                return false;
            }
        }
    }

    @Override
    public void run() {
        this.Receivefile();
    }

}
