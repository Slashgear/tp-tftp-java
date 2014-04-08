/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tftpclient;

import tftpclient.View.Fmain;

/**
 *
 * @author Antoine
 */
public class TFTPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TFTPSend test=new TFTPSend();
        if(test.Sendfile()==0){
            System.out.println("Transfers Réussi");
        }
    }
    
}
