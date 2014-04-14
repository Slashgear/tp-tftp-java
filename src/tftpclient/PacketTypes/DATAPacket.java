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
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antoine
 */
public class DATAPacket extends TFTPPacket {

    public DATAPacket(byte[] data, InetAddress ip, int port, int number) {
        _dtg = createDATAPacket(data, ip, port, number);
    }

    public DatagramPacket createDATAPacket(byte[] data, InetAddress ip, int port, int number) {
        try {
            byte[] tftp_opcode = intTobyte2(3);
            byte[] tftp_block_nb = intTobyte2(number);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(tftp_opcode);
            outputStream.write(tftp_block_nb);
            outputStream.write(data);
            
            
            byte[] tftp_data = outputStream.toByteArray();
            System.out.println("DATA :"+Arrays.toString(tftp_data));

            return new DatagramPacket(tftp_data, tftp_data.length, ip, port);
        } catch (IOException ex) {
            Logger.getLogger(DATAPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int getBlockNb(DatagramPacket _dtg){
        ByteBuffer data = ByteBuffer.allocate(2);
        data.put(_dtg.getData()[2]);
        data.put((int)1,_dtg.getData()[3]);
        return (int)data.getShort(0);
    }
    
    public byte[] getData(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for(int i=4;i<_dtg.getData().length;i++){
            outputStream.write(_dtg.getData()[i]);
        }
        return outputStream.toByteArray();
    }

    public static boolean isDataPacket(DatagramPacket _dtg) {
        return 3 == getOpcode(_dtg);
    }
}
