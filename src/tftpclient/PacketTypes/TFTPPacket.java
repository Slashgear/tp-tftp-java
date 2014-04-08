/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftpclient.PacketTypes;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

/**
 *
 * @author Antoine
 */
public class TFTPPacket {

    protected DatagramPacket _dtg;

    public void setDtg(DatagramPacket _dtg) {
        this._dtg = _dtg;
    }

    public DatagramPacket getDtg() {
        return _dtg;
    }

    public TFTPPacket() {
    }

    public byte[] intTobyte2(int i) {
        
        ByteBuffer data = ByteBuffer.allocate(2);
        data.putShort((short)i);
        return data.array();
    }

    public int getOpcode() {
        ByteBuffer data = ByteBuffer.allocate(2);
        data.put(_dtg.getData()[0]);
        data.put((int)1,_dtg.getData()[1]);
        return (int)data.get();
    }
}
