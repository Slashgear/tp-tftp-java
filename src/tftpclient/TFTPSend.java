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

    public TFTPSend() {
        super();

        this._port_dest = 69;
        filename = "platine-noirblanc.jpg";
        this._ip = (Inet4Address) InetAddress.getLoopbackAddress();

    }

    public boolean checkFile() {
        file = new File(filename);
        return file.exists() && file.canRead();
    }

    public char Sendfile() {

        if (!checkFile()) { // Test if the File is reachable and if the File is readable
            return 1;
        } else {
            System.out.println("Fichier Correct");
            try {
                if (!_ip.isReachable(5000)) { // test if the Server Adress exist, and if it's reachable
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
        }
        return 126;
    }

    public void sendWRQ() {
        WriteRequestPacket wrq_packet = new WriteRequestPacket(null, _ip, _port_dest, filename);
        try {
            this._socket.send(wrq_packet.getDtg());
        } catch (IOException ex) {
            System.out.println("Echec sendWRQ");
        }
    }

    public boolean receiveWRQanswer() {
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
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean WRQtry() {
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

    public boolean transmit() {
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
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
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
                _socket.send(data_packet.getDtg());
                _socket.receive(ack_dtg);
                //Test if the answer is correct
                if (ACKPacket.isACKPacket(ack_dtg) && j == ACKPacket.getBlockNb(ack_dtg)) {
                    System.out.println("ACK  :" + Arrays.toString(ack_dtg.getData()));
                    packet_lost = true;
                    return true;
                }
            } catch (IOException ex) {
                Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
        return packet_lost;
    }

}
