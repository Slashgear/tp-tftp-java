/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftpclient.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antoine
 */
public class ACKPacket extends TFTPPacket {

    public ACKPacket(InetAddress ip, int port, int number) {
        _dtg = createACKPacket(ip, port, number);
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
            Logger.getLogger(ACKPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int getBlockNb() {
        int i = (_dtg.getData()[3] << 8) & 0xFF | (_dtg.getData()[2]) & 0xFF;
        return i;
    }
}
