/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcptasca3;

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
public class Servidor extends Thread {

    ServerSocket socket_s = null;
    Socket sc = null;
    DataInputStream input;
    DataOutputStream output;
    String mensaje = "";
    final int puertoServidor = 5000;

    DatosDeAcceso datos = null;
    boolean comprobarUser = false;
    boolean comprobarPass = false;

    public Servidor(DatosDeAcceso datos) {
        this.datos = datos;
    }

    @Override
    public void run() {
        try {            
            
            this.socket_s = new ServerSocket(this.puertoServidor);
            System.out.println("[SERVIDOR] Iniciado");
            //Entra en espera de la conexion de un cliente...
            sc = this.socket_s.accept();
            System.out.println("[SERVIDOR] Se me ha conectado un cliente");
            input = new DataInputStream(sc.getInputStream());
            output = new DataOutputStream(sc.getOutputStream());

            while (this.comprobarPass == false || this.comprobarUser == false) {
                mensaje = input.readUTF();

                System.out.println("[SERVIDOR] He recibido del cliente " + mensaje);
                procesarLogin(mensaje);
                //Lo siguiente se hace para que el cliente sepa el dato que necesita por enviar -usuario -pass o -fin
                output.writeUTF(this.generarRespuesta());
            }
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(this.datos.getContraseña().getBytes());
            String encryptedString = new String(messageDigest.digest());
            System.out.println("Usuario " + this.datos.getUsuario() + "logeado correctamente con contraseña " + encryptedString);
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void procesarLogin(String mensaje) { // el mensaje llegará en formato key : value
        String[] array = mensaje.split(":");
        if (this.comprobarUser == false) { // si todavia no se ha validado el usuario
            if (array[0].equalsIgnoreCase("usuario")) {
                if (array[1].equalsIgnoreCase(datos.getUsuario())) {
                    this.comprobarUser = true;
                    System.out.println("[SERVIDOR] Usuario correcto");
                } else {
                    System.out.println("[SERVIDOR] Usuario incorrecto");
                }
            } else {
                System.out.println("[SERVIDOR] Introducido contraseña cuando se esperaba usuario!");
            }
        } else if (this.comprobarPass == false && this.comprobarUser == true) { // si todavia no se ha validado la contraseña
            if (array[0].equalsIgnoreCase("contraseña")) {
                if (array[1].equalsIgnoreCase(datos.getContraseña())) {
                    this.comprobarPass = true;
                    System.out.println("[SERVIDOR] Contraseña correcta");
                } else {
                    System.out.println("[SERVIDOR] Contraseña incorrecta");
                }
            } else {
                System.out.println("[SERVIDOR] Introducido usuario cuando se esperaba contraseña!");
            }
        }

    }

    public String generarRespuesta() {
        if (this.comprobarUser == false) {
            return "usuario";
        } else if (this.comprobarPass == false) {
            return "contraseña";
        } else {
            return "fin";
        }
    }

}
