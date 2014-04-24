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
import java.util.logging.Level;
import java.util.logging.Logger;
import tftpclient.PacketTypes.ACKPacket;
import tftpclient.PacketTypes.DATAPacket;
import tftpclient.PacketTypes.ERRORPacket;
import tftpclient.PacketTypes.WriteRequestPacket;

/**
 *
 * @author Antoine CARON
 */
public class TFTPSend extends TFTPTransaction {

    
    private File file;

    public TFTPSend(File file,String ip) {
        super();

        this._port_dest = 69;
        this.file=file;
        try {
            this._ip = (Inet4Address) InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public char Sendfile() {
            System.out.println("Fichier Correct");
            try {
                if (!_ip.isReachable(10000)) { // test if the Server Adress exist, and if it's reachable
                    System.out.println("Host non joignable");
                    return 2;
                } else {
                    System.out.println("Adresse Correct");
                    if (!WRQtry()) {
                        return 3;
                    } else {
                        //Start the transmit of the file
                        System.out.println("Envoi du WRQ réussi \n\nDébut du Transfert...\n");
                        if (!this.transmit()) {
                            return 4;
                        } else {
                            return 0;
                        }
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
            }
        return 126;
    }

    private void sendWRQ() {
        WriteRequestPacket wrq_packet = new WriteRequestPacket(null, _ip, _port_dest, file.getName());
        try {
            this._socket.send(wrq_packet.getDtg());
        } catch (IOException ex) {
            System.out.println("Echec sendWRQ");
        }
    }

    private boolean receiveWRQanswer() {
        byte[] data = new byte[1024];
        DatagramPacket dtg = new DatagramPacket(data, 1024);
        try {
            this._socket.receive(dtg);
            System.out.println("ACK Answer:" + Arrays.toString(dtg.getData()));
            ERRORPacket er;
            if (ACKPacket.isACKPacket(dtg) || (ERRORPacket.isErrorPacket(dtg) && 6 == ERRORPacket.getErrorCode(dtg))) {
                _port_dest = dtg.getPort();
                return true;
            }
        } catch (IOException ex) {
            System.out.println("Ack non reçu");
        }
        return false;
    }

    private boolean WRQtry() {
        int i = 0;
        boolean answer = false;
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
        int j = 0;
        boolean packet_lost = false;
        try {
            byte[] data = new byte[1024];
            DatagramPacket dtg = new DatagramPacket(data, 1024);
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
        } catch (IOException ex) {
            System.out.println("Echec lecture du fichier");
        }
        return false;
    }

    public boolean transmitPacket(byte[] buf, int j) {
        int i = 0;
        boolean packet_lost = false;
        DatagramPacket ack_dtg = new DatagramPacket(new byte[1024], 1024);

        while (i < 3 || packet_lost) {
            DATAPacket data_packet = new DATAPacket(buf, _ip, _port_dest, j);
            try {
                System.out.println("DATA :"+Arrays.toString(data_packet.getData()));
                _socket.send(data_packet.getDtg());
                _socket.receive(ack_dtg);
                //Test if the answer is correct
                if (ACKPacket.isACKPacket(ack_dtg) && j == ACKPacket.getBlockNb(ack_dtg)) {
                    System.out.println("ACK  :" + Arrays.toString(ack_dtg.getData()));
                    packet_lost = true;
                    return true;
                }
            } catch (IOException ex) {
                System.out.println("Echec transfert du paquet");
            }
            i++;
        }
        return packet_lost;
    }

}
