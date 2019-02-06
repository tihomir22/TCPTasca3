/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcptasca3.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mati
 */
public class HiloServidorTh extends Thread {

    Socket socker_server;
    DataInputStream input;
    DataOutputStream output;
    String name, mensaje, usuario, contraseña;
    public boolean comprobarUser = false;
    public boolean comprobarPass = false;

    DatosDeAcceso datos = null;

    public HiloServidorTh(Socket socker_server, String nombre, DatosDeAcceso datos) {
        this.socker_server = socker_server;
        this.name = nombre;
        this.datos = datos;
    }

    @Override
    public void run() {
        try {
            while (!this.generarRespuesta().equalsIgnoreCase("fin")) {
                System.out.println("[HILO SERVIDOR " + this.name + "] Se me ha conectado un cliente");
                input = new DataInputStream(socker_server.getInputStream());
                output = new DataOutputStream(socker_server.getOutputStream());

                mensaje = input.readUTF();

                System.out.println("[HILO SERVIDOR " + this.name + "] He recibido del cliente " + mensaje);
                procesarLogin(mensaje, output);

            }
            output.close();
            input.close();

        } catch (IOException ex) {
            Logger.getLogger(HiloServidorTh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HiloServidorTh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void procesarLogin(String mensaje, DataOutputStream output) throws IOException, NoSuchAlgorithmException { // el mensaje llegará en formato key : value
        String[] array = mensaje.split(":");
        if (comprobarUser == false) { // si todavia no se ha validado el usuario
            if (array[0].equalsIgnoreCase("usuario")) {
                if (array[1].equalsIgnoreCase(datos.getUsuario())) {
                    comprobarUser = true;
                    usuario = array[1];
                    System.out.println("[HILO SERVIDOR " + this.name + "] Usuario correcto");
                    output.writeUTF(this.generarRespuesta() + ":Usuario correcto");

                } else {
                    System.out.println("[HILO SERVIDOR " + this.name + "] Usuario incorrecto");
                    output.writeUTF(this.generarRespuesta() + ":Usuario Incorrecto");

                }
            } else {
                System.out.println("[HILO SERVIDOR " + this.name + "] Introducido contraseña cuando se esperaba usuario!");
                output.writeUTF(this.generarRespuesta() + ":Introducido contraseña cuando se esperaba usuario");
            }
        } else if (comprobarPass == false && comprobarUser == true) { // si todavia no se ha validado la contraseña
            if (array[0].equalsIgnoreCase("contraseña")) {
                if (array[1].equalsIgnoreCase(datos.getContraseña())) {
                    comprobarPass = true;
                    contraseña = array[1];
                    System.out.println("[HILO SERVIDOR " + this.name + "] Contraseña correcta");
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(contraseña.getBytes());
                    String encryptedString = new String(messageDigest.digest());
                    output.writeUTF(this.generarRespuesta() + ":El usuario " + usuario + " se ha logeado con exito con la pass " + encryptedString);
                } else {
                    System.out.println("[HILO SERVIDOR " + this.name + "] Contraseña incorrecta");
                    output.writeUTF(this.generarRespuesta() + ":Contraseña Incorrecta");
                }
            } else {
                System.out.println("[HILO SERVIDOR " + this.name + "] Introducido usuario cuando se esperaba contraseña!");
                output.writeUTF(this.generarRespuesta() + ":Introducido usuario cuando se esperaba contraseña");

            }
        } else {

        }

    }

    public String generarRespuesta() {
        if (comprobarUser == false) {
            return "usuario";
        } else if (comprobarPass == false) {
            return "contraseña";
        } else {
            return "fin";
        }
    }

}
