package tftpclient.PacketTypes;

import java.net.InetAddress;

/**
 *
 * @author Antoine
 */
public class WriteRequestPacket extends RequestPacket {

    public WriteRequestPacket(String _directory, InetAddress ip, int port, String filename) {
        super(_directory);
        _dtg = createRequestPacket(ip, port, filename, 2);
    }
}
