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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antoine
 */
public class ERRORPacket extends TFTPPacket{

    public ERRORPacket(InetAddress ip, int port, int errorCode) {
        _dtg = createERRORPacket(ip, port, errorCode);
    }

    private DatagramPacket createERRORPacket(InetAddress ip, int port, int errorCode) {
        try {
            byte[] tftp_opcode = intTobyte2(3);
            byte[] tftp_block_nb = intTobyte2(errorCode);
            byte[] tftp_message= "Error".getBytes();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(tftp_opcode);
            outputStream.write(tftp_block_nb);
            outputStream.write(tftp_message);
            byte[] tftp_data = outputStream.toByteArray();

            return new DatagramPacket(tftp_data, tftp_data.length, ip, port);
        } catch (IOException ex) {
            Logger.getLogger(ACKPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    }
    
    public static boolean isErrorPacket(DatagramPacket _dtg){
        return 5== getOpcode(_dtg);
    }
    
    public static int getErrorCode(DatagramPacket _dtg){
        ByteBuffer data = ByteBuffer.allocate(2);
        data.put(_dtg.getData()[2]);
        data.put((int)1,_dtg.getData()[3]);
        return (int)data.get();
    }
    public static String getErrMsg(DatagramPacket _dtg){
        StringBuilder sbuf=new StringBuilder();
       for(int i= 4;i< _dtg.getData().length;i++){
           sbuf.append((char)_dtg.getData()[i]);
       }
       return sbuf.toString();
    }
}
