
package tftpclient.Observers;

import java.io.File;
import tftpclient.TFTPTransaction;

/**
 *
 * @author Antoine
 */
public interface TFTPObserver {
    public void onFileSendingStarted(File sourceFile);
    public void onFileSendingProgress(float percent);
    public void onFileSendingEnded(TFTPTransaction client, File sourceFile);
    public void onFileReceptionStarted(String remoteFileName);
    public void onFileReceptionEnded(TFTPTransaction client, File holder);
    public void onErrorOccured(String errormsg);
}
