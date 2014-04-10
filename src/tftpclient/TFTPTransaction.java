
package tftpclient;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antoine
 */
public class TFTPTransaction {
    
    protected DatagramSocket _socket;
    protected Inet4Address _ip;
    protected int _port_dest;
    protected String filename;

    public TFTPTransaction() {
        try {
            _socket = new DatagramSocket();
            _socket.setSoTimeout(10000);
        } catch (SocketException ex) {
            System.out.println("Echec ouvertur du socket");
        }
    }
 
    
}
