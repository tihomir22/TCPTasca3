/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcptasca3.threads;

import tcptasca3.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author sportak
 */
public class TCPTasca3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        DatosDeAcceso acceso = new DatosDeAcceso("laura", "l123");
        ServidorTh server = new ServidorTh(acceso);
        server.start();
        Thread.sleep(100);
        //Cliente cli1 = new Cliente();
        //cli1.start();
    }

}
