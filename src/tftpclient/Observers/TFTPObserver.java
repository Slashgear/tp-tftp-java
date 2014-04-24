
package tftpclient.Observers;

import java.io.File;
import tftpclient.TFTPTransaction;

/**
 *
 * @author Antoine
 */
public interface TFTPObserver {
    public void onErrorOccured(String errormsg);
    public void onInfoSending(String infoMsg);
    public void onSendingEnd(char valeur);
    public void onReceivingEnd(char valeur);
}
