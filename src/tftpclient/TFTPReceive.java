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
    private static final int DEFAULT_PACKET_SIZE = 516;

    public TFTPReceive() {
        super();
        this._port_dest = 69;
        filename = "Damier.bmp";
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
                    if (transmit(dtg)) {
                        return 0;
                    } else {
                        return 3;
                    }
                }
            }
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
        byte[] data = new byte[DEFAULT_PACKET_SIZE];
        DatagramPacket dtg = new DatagramPacket(data, data.length);
        try {
            this._socket.receive(dtg);
            if (DATAPacket.isDataPacket(dtg)) {
                _port_dest = dtg.getPort();
                byte[] truncate = new byte[dtg.getLength() - 4];
                System.arraycopy(dtg.getData(), 4, truncate, 0, dtg.getLength() - 4);
                return new DATAPacket(truncate, dtg.getAddress(), dtg.getPort(), 1);
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

    public boolean transmit(DATAPacket dtg) {
        FileOutputStream fos = null;
        try {
            int i = 1;
            File fichier = new File(filename);
            DatagramPacket _dtg = new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
            fichier.createNewFile();
            fos = new FileOutputStream(fichier);
            fos.write(dtg.getData());
            ACKPacket ack = new ACKPacket(_ip, dtg.getDtg().getPort(), ACKPacket.getBlockNb(dtg.getDtg()));
            _socket.send(ack.getDtg());
            System.out.println("ACK  :" + Arrays.toString(ack.getDtg().getData()));
            if (dtg.getDtg().getLength() < 512) {
                return true;
            }
            while (true) {
                _socket.receive(_dtg);
                if (ERRORPacket.isErrorPacket(_dtg)) {
                    System.out.println("Une Erreur est survenue: " + ERRORPacket.getErrMsg(_dtg));
                    fos.close();
                    return false;
                } else {
                        System.out.println("DATA :" + Arrays.toString(_dtg.getData()));
                        byte[] truncate = new byte[_dtg.getLength() - 4];
                        System.arraycopy(_dtg.getData(), 4, truncate, 0, _dtg.getLength() - 4);
                        fos.write(truncate);
                        ack = new ACKPacket(_ip, dtg.getDtg().getPort(), DATAPacket.getBlockNb(_dtg));
                        System.out.println("ACK  :" + Arrays.toString(ack.getDtg().getData()));
                        _socket.send(ack.getDtg());
                        i++;
                        if (_dtg.getLength() < 512) {
                            break;
                        }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Fichier non-ouvrable");
            return false;
        } catch (IOException ex) {
            System.out.println("Délai d'attente dépassé");
            return false;
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                System.out.println("Fermeture du fichier impossible");
            }
            return true;
        }
    }

}
