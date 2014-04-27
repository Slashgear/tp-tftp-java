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

    public TFTPObservable() {
        observers = new LinkedList<TFTPObserver>();
    }

    public void addObserver(TFTPObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TFTPObserver observer) {
        observers.remove(observer);
    }

    public void removeObservers() {
        observers.clear();
    }

    protected void fireErrorOccured(String errormsg) {
        for (TFTPObserver o : observers) {
            o.onErrorOccured(errormsg);
        }
    }

    protected void fireInfoSending(String infomsg) {
        for (TFTPObserver o : observers) {
            o.onInfoSending(infomsg);
        }
    }

    protected void fireSendingEnd(char value) {
        for (TFTPObserver o : observers) {
            o.onSendingEnd(value);
        }
    }

    protected void fireReceivingEnd(char value) {
        for (TFTPObserver o : observers) {
            o.onSendingEnd(value);
        }
    }
    
    protected void fireProccessingSend(int value){
        for (TFTPObserver o : observers) {
            o.onProcessingSend(value);
        }
    }
}
