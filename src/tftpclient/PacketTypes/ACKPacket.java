package tftpclient.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Paquet d'Acknolegement de TFTP
 * @author Antoine
 */
public class ACKPacket extends TFTPPacket {

    /**
     * Contructeur du paquet ACK
     * @param ip Adresse du destinataire
     * @param port port du destinataire
     * @param number numéro de l'ACK
     */
    public ACKPacket(InetAddress ip, int port, int number) {
        _dtg = createACKPacket(ip, port, number);
    }

    public ACKPacket() {

    }

    private DatagramPacket createACKPacket(InetAddress ip, int port, int number) {
        try {
            byte[] tftp_opcode = intTobyte2(4);
            byte[] tftp_block_nb = intTobyte2(number);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(tftp_opcode);
            outputStream.write(tftp_block_nb);
            byte[] tftp_data = outputStream.toByteArray();

            return new DatagramPacket(tftp_data, tftp_data.length, ip, port);
        } catch (IOException ex) {
            System.out.println("Erreur lors de la création de l'ACKpacket n°" + number);
        }
        return null;
    }

    public static int getBlockNb(DatagramPacket _dtg) {
        ByteBuffer data = ByteBuffer.allocate(2);
        data.put(_dtg.getData()[2]);
        data.put((int) 1, _dtg.getData()[3]);
        return (int) data.getShort(0);
    }

    public static boolean isACKPacket(DatagramPacket _dtg) {
        return 4 == getOpcode(_dtg);
    }
}
