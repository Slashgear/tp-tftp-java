package tftpclient;

import java.io.File;
import java.io.FileInputStream;
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
import tftpclient.PacketTypes.TFTPPacket;
import tftpclient.PacketTypes.WriteRequestPacket;

/**
 *
 * @author Antoine
 */
public class TFTPSend extends TFTPTransaction {

    private String filename;
    public TFTPSend() {
        super();
        
            this._port_dest = 69;
            filename="platine-noirblanc.jpg";
        try {
            this._ip = (Inet4Address) InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    public boolean checkFile() {
        return true;
    }

    public char Sendfile() {

        if (!checkFile()) {
            return 1;
        } else {
            System.out.println("Fichier Correct");
            try {
                if (!_ip.isReachable(5000)) {
                    return 2;
                } else {
                    System.out.println("Adresse Correct");
                    if (!WRQtry()) {
                        return 3;
                    } else {
                            // DTG for receiving the answer of the server
                            byte[] data = new byte[1024];
                            DatagramPacket dtg = new DatagramPacket(data, 1024);
                            
                            FileInputStream fis=new FileInputStream(new File(filename));
                            byte[] buf=new byte[512];
                            int i=1;
                            while(fis.read(buf)>=0){
                                DATAPacket data_packet=new DATAPacket(buf, _ip, _port_dest,i);
                                _socket.send(data_packet.getDtg());
                                i++;
                            }
                            DATAPacket data_packet=new DATAPacket(new byte[512], _ip, _port_dest,i);
                            _socket.send(data_packet.getDtg());
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
        ACKPacket ack_packet = new ACKPacket();
        byte[] data = new byte[1024];
        DatagramPacket dtg = new DatagramPacket(data, 1024);
        try {
            this._socket.receive(dtg);
            ack_packet.setDtg(dtg);
            System.out.println("ACK Answer:" +Arrays.toString(dtg.getData()));
            if (ack_packet.isACKPacket()) {
                _port_dest = ack_packet.getDtg().getPort();
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
    

}
