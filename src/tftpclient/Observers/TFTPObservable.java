
package tftpclient.Observers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import tftpclient.TFTPTransaction;

/**
 *
 * @author Antoine
 */
public class TFTPObservable {
    private List<TFTPObserver> observers;
    
    public TFTPObservable(){
        observers = new LinkedList<TFTPObserver>();
    }

    public void addObserver(TFTPObserver observer){
        observers.add(observer);
    }

    public void removeObserver(TFTPObserver observer){
        observers.remove(observer);
    }

    public void removeObservers(){
        observers.clear();
    }

    protected void fireFileReceptionStarted(String remoteFileName){
        for(TFTPObserver o : observers){
           o.onFileReceptionStarted(remoteFileName);
        }
    }

    protected void fireFileReceptionEnded(final TFTPTransaction client,final File holder){
        for(TFTPObserver o : observers){
            o.onFileReceptionEnded(client, holder);
        }
    }

    protected void fireFileSendingStarted(File sourceFile){
        for(TFTPObserver o : observers){
            o.onFileSendingStarted(sourceFile);
        }
    }

    protected void fireFileSendingProgress(float percent){
        for(TFTPObserver o : observers){
            o.onFileSendingProgress(percent);
        }
    }

    protected void fireFileSendingEnded(final TFTPTransaction client, final File sourceFile){
        for(TFTPObserver o : observers){
            o.onFileSendingEnded(client, sourceFile);
        }
    }

    protected void fireErrorOccured(String errormsg){
        for(TFTPObserver o : observers){
            o.onErrorOccured(errormsg);
        }
    }
}
