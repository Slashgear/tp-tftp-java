
package tftpclient;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import tftpclient.Observers.TFTPObservable;

/**
 *
 * @author Antoine
 */
public abstract class TFTPTransaction extends TFTPObservable implements Runnable{
    
    protected DatagramSocket _socket;
    protected Inet4Address _ip;
    protected int _port_dest;

    public TFTPTransaction() {
        try {
            _socket = new DatagramSocket();
            _socket.setSoTimeout(10000);
        } catch (SocketException ex) {
            System.out.println("Echec ouvertur du socket");
        }
    }
 
    
}
