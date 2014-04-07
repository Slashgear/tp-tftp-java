/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tftpclient.PacketTypes;

import java.net.InetAddress;

/**
 *
 * @author Antoine
 */
public class ReadRequestPacket extends RequestPacket{

    public ReadRequestPacket(String _directory, InetAddress ip, int port, String filename, int opcode) {
        super(_directory, ip, port, filename, opcode);
        _dtg=createRequestPacket(ip, port, filename, 1);
    }
}
