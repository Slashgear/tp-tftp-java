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
public class WriteRequestPacket extends RequestPacket {

    public WriteRequestPacket(String _directory, InetAddress ip, int port, String filename, int opcode) {
        super(_directory, ip, port, filename, opcode);
        _dtg=createRequestPacket(ip, port, filename,2);
    }
}
