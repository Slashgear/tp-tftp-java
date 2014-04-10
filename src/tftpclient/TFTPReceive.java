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
import tftpclient.PacketTypes.WriteRequestPacket;

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
        char Crrv;
        File fichier = new File(filename);
        try {
            fichier.createNewFile();
            FileOutputStream fos = new FileOutputStream(fichier);

        } catch (FileNotFoundException ex) {
            System.out.println("Fichier introuvé");
        } catch (IOException ex) {
            System.out.println("Fichier inouvrable");
        }
        return 126;
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
        byte[] data = new byte[1024];
        DatagramPacket dtg = new DatagramPacket(data, 1024);
        try {
            this._socket.receive(dtg);
            System.out.println("ACK Answer:" + Arrays.toString(dtg.getData()));
            if (DATAPacket.isDataPacket(dtg)) {
                _port_dest = dtg.getPort();
                return new DATAPacket(dtg.getData(), dtg.getAddress(), dtg.getPort(), 1);
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

}
