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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antoine
 */
public abstract class RequestPacket extends TFTPPacket{
    
    protected String _directory;

    public RequestPacket(String _directory) {
        this._directory = _directory;
    }
    
    public DatagramPacket createRequestPacket(InetAddress ip, int port, String filename,int opcode){
        try {
            byte zero=(byte)0;
            byte[] tftp_opcode = intTobyte2(opcode);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(tftp_opcode);
            if(_directory!=null){
                outputStream.write(_directory.getBytes());
            }
            outputStream.write(filename.getBytes());
            outputStream.write(zero);
            outputStream.write("octet".getBytes());
            outputStream.write(zero);
            byte[] tftp_data = outputStream.toByteArray();
            System.out.println("Request :"+Arrays.toString(tftp_data));
            return new DatagramPacket(tftp_data, tftp_data.length, ip, port);
        } catch (IOException ex) {
            Logger.getLogger(ACKPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    
    public String getFilename(){
        int i=2;
        StringBuilder buf= new StringBuilder();
        while(_dtg.getData()[i]!=(byte)0){
             buf.append((char) _dtg.getData()[i]);
        }       
        return buf.toString();
    }
}
