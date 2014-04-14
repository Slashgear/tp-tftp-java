package tftpclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import tftpclient.PacketTypes.ReadRequestPacket;

/**
 *
 * @author Antoine
 */
public class TFTPReceive extends TFTPTransaction {

    private String directory_name;

    public TFTPReceive() {
        super();
        this._port_dest = 69;
        filename = "test.txt";
        try {
            this._ip = (Inet4Address) InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public char Receivefile() {
        try {
            if (!_ip.isReachable(5000)) {
                return 1;
            } else {
                DATAPacket dtg = RRQtry();
                if (dtg == null) {
                    return 2;
                } else {
                    transmit(dtg);
                }
            }
            return 126;
        } catch (IOException ex) {
            System.out.println("Adress non joignable");
        }
        return 125;
    }

    public void sendRRQ() {
        ReadRequestPacket rrq_packet = new ReadRequestPacket(null, _ip, _port_dest, filename);
        try {
            this._socket.send(rrq_packet.getDtg());
        } catch (IOException ex) {
            System.out.println("Echec sendRRQ");
        }
    }

    public DATAPacket receiveRRQanswer() {
        byte[] data = new byte[512];
        DatagramPacket dtg = new DatagramPacket(data, data.length);
        try {
            this._socket.receive(dtg);
            if (DATAPacket.isDataPacket(dtg)) {
                _port_dest = dtg.getPort();
                System.out.println(dtg.getLength());
                byte[] truncate=new byte[dtg.getLength()-4];
                System.arraycopy(dtg.getData(), 4, truncate, 0,dtg.getLength()-4);
                return new DATAPacket(truncate,dtg.getAddress(),dtg.getPort(), 1);
            }
            if (ERRORPacket.isErrorPacket(dtg) && (1 != ERRORPacket.getErrorCode(dtg) || 2 != ERRORPacket.getErrorCode(dtg))) {
                System.out.println(ERRORPacket.getErrMsg(dtg));
                return null;
            }
        } catch (IOException ex) {
            System.out.println("Ack non reçu");
        }
        return null;
    }

    public DATAPacket RRQtry() {
        int i = 0;
        DATAPacket dtg;
        while (i < 3) {
            sendRRQ();
            dtg = receiveRRQanswer();
            if (dtg != null) {
                return dtg;
            } else {
                ++i;
            }
        }
        return null;
    }
    
    public boolean transmit(DATAPacket dtg){
        FileOutputStream fos = null;
        try {
            int i=0;
            File fichier = new File(filename);
            DatagramPacket _dtg=new DatagramPacket(new byte[512], 512);
            fichier.createNewFile();
            fos = new FileOutputStream(fichier);
            fos.write(dtg.getData());
            ACKPacket ack=new ACKPacket(_ip,dtg.getDtg().getPort(),ACKPacket.getBlockNb(dtg.getDtg()));
            _socket.send(ack.getDtg());
            System.out.println("ACK  :" + Arrays.toString(ack.getDtg().getData()));
            if(_dtg.getLength()<512){
                    return true;
            }
            while(true){
                _socket.receive(_dtg);
                System.out.println(Arrays.toString(_dtg.getData()));
                ack=new ACKPacket(_ip,dtg.getDtg().getPort(),DATAPacket.getBlockNb(_dtg));
                _socket.send(ack.getDtg());       
                if(_dtg.getLength()<512){
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TFTPReceive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TFTPReceive.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(TFTPReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

}
