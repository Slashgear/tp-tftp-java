/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tftpclient.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;

/**
 *
 * @author Antoine
 */
public class TFTPPacket {
    protected DatagramPacket _dtg;
    
     public byte[] intTobyte2(int i){
        byte[] tab= new byte[2];
        tab[0] = (byte) (i & 0xFF);
        tab[1] = (byte) ((i >> 8) & 0xFF);
        return tab;
    }
     public int getOpcode(){
        int i=(_dtg.getData()[1]<<8)&0xFF|(_dtg.getData()[0]<<0)&0xFF;
        return  i;
     }
}
