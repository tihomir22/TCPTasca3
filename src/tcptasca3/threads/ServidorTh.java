/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcptasca3.threads;

import tcptasca3.*;
import static com.sun.imageio.plugins.common.LZWStringTable.hash;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.util.Objects.hash;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class ServidorTh extends Thread {

    ServerSocket socket_s = null;
    Socket sc = null;
    DataInputStream input;
    DataOutputStream output;
    String mensaje = "";
    final int puertoServidor = 5000;
    public static boolean comprobarUser = false;
    public static boolean comprobarPass = false;
    DatosDeAcceso datos;

    public ServidorTh(DatosDeAcceso datos) {
        this.datos = datos;
    }

    @Override
    public void run() {
        try {

            this.socket_s = new ServerSocket(this.puertoServidor);
            System.out.println("[SERVIDOR] Iniciado");
            //Entra en espera de la conexion de un cliente...

            while (comprobarPass == false || comprobarUser == false) {
                sc = this.socket_s.accept();
                HiloServidorTh hilo = new HiloServidorTh(sc, "Hilo" + Math.round(Math.random() * 100), datos);
                hilo.start();
                hilo.join();

            }
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(this.datos.getContraseña().getBytes());
            String encryptedString = new String(messageDigest.digest());
            System.out.println("Usuario " + this.datos.getUsuario() + "logeado correctamente con contraseña " + encryptedString);
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorTh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServidorTh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServidorTh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
