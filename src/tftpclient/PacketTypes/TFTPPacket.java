package tftpclient.PacketTypes;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

/**
 *  Paquet Générique du protocole TFTP
 * @author Antoine
 */
public class TFTPPacket {
    /**
     * DatagrammePacket qui contient les données du paquet
     */
    protected DatagramPacket _dtg;

    public void setDtg(DatagramPacket _dtg) {
        this._dtg = _dtg;
    }
    /**
     * assesseur du DatagramPacket
     * @return DatagramPacket
     */
    public DatagramPacket getDtg() {
        return _dtg;
    }
    /**
     * Constructeur par défaut
     */
    public TFTPPacket() {
    }
    
    /**
     *  Fonction qui converti un entier en un tableau de Byte de taille deux
     * @param i entier à convertir
     * @return byte[] entier converti
     */
    public byte[] intTobyte2(int i) {
        ByteBuffer data = ByteBuffer.allocate(2);
        data.putShort((short) i);
        return data.array();
    }
    /**
     * Fonction qui récupère l'OPCODE du paquet TFTP
     * @param _dtg un DatagramPacket
     * @return int Opcode
     */
    public static int getOpcode(DatagramPacket _dtg) {
        ByteBuffer data = ByteBuffer.allocate(2);
        data.put(_dtg.getData()[0]);
        data.put((int) 1, _dtg.getData()[1]);
        return (int) data.get();
    }
}
