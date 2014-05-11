package tftpclient;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import tftpclient.Observers.TFTPObservable;

/**
 * Objet Définissant une Transaction non Typé du protocole TFTP
 * @author Antoine
 */
public abstract class TFTPTransaction extends TFTPObservable implements Runnable {
    /**
     * Socket utilisé par l'application pour envoyer et recevoir des données
     */
    private DatagramSocket _socket;
    /**
     * Adresse IP du serveur Destinataire
     */
    private Inet4Address _ip;
    /**
     * Port de Destination de Serveur Distant
     */
    private int _port_dest;

    public Inet4Address getIp() {
        return _ip;
    }

    public void setIp(Inet4Address _ip) {
        this._ip = _ip;
    }

    public int getPort_dest() {
        return _port_dest;
    }

    public void setPort_dest(int _port_dest) {
        this._port_dest = _port_dest;
    }

    public DatagramSocket getSocket() {
        return _socket;
    }

    
    /**
     * Constructeur de la Transaction, il initialise le socket
     */
    public TFTPTransaction() {
        try {
            _socket = new DatagramSocket();
            this._port_dest=69;
            //On défini un TimeOut de 10sec pour la réponse du serveur
            _socket.setSoTimeout(10000);
        } catch (SocketException ex) {
            System.out.println("Echec ouverture du socket");
        }
    }

}
