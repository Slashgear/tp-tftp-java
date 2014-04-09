
package tftpclient;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antoine
 */
public class TFTPReceive extends TFTPTransaction{

    private String directory_name;
    
    public TFTPReceive() {
        super();
        this._port_dest = 69;
        filename = "platine-noirblanc.jpg";
        try {
            this._ip = (Inet4Address) InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TFTPSend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
