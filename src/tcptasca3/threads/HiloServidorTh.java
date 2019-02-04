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
    String name, mensaje;

    DatosDeAcceso datos = null;

    public HiloServidorTh(Socket socker_server, String nombre, DatosDeAcceso datos) {
        this.socker_server = socker_server;
        this.name = nombre;
        this.datos = datos;
    }

    @Override
    public void run() {
        try {
            while (!this.generarRespuesta(0).split(":")[1].equalsIgnoreCase("fin")) {
                System.out.println("[HILO SERVIDOR " + this.name + "] Se me ha conectado un cliente");
                input = new DataInputStream(socker_server.getInputStream());
                output = new DataOutputStream(socker_server.getOutputStream());

                mensaje = input.readUTF();

                System.out.println("[HILO SERVIDOR " + this.name + "] He recibido del cliente " + mensaje);
                procesarLogin(mensaje, output);
                //Lo siguiente se hace para que el cliente sepa el dato que necesita por enviar -usuario -pass o -fin

            }
            output.close();
            input.close();

        } catch (IOException ex) {
            Logger.getLogger(HiloServidorTh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void procesarLogin(String mensaje, DataOutputStream output) throws IOException { // el mensaje llegará en formato key : value
        String[] array = mensaje.split(":");
        if (ServidorTh.comprobarUser == false) { // si todavia no se ha validado el usuario
            if (array[0].equalsIgnoreCase("usuario")) {
                if (array[1].equalsIgnoreCase(datos.getUsuario())) {
                    ServidorTh.comprobarUser = true;
                    System.out.println("[HILO SERVIDOR " + this.name + "] Usuario correcto");
                    output.writeUTF(this.generarRespuesta(1));

                } else {
                    System.out.println("[HILO SERVIDOR " + this.name + "] Usuario incorrecto");
                    output.writeUTF(this.generarRespuesta(0));

                }
            } else {
                System.out.println("[HILO SERVIDOR " + this.name + "] Introducido contraseña cuando se esperaba usuario!");
                output.writeUTF(this.generarRespuesta(0));
            }
        } else if (ServidorTh.comprobarPass == false && ServidorTh.comprobarUser == true) { // si todavia no se ha validado la contraseña
            if (array[0].equalsIgnoreCase("contraseña")) {
                if (array[1].equalsIgnoreCase(datos.getContraseña())) {
                    ServidorTh.comprobarPass = true;
                    System.out.println("[HILO SERVIDOR " + this.name + "] Contraseña correcta");
                    output.writeUTF(this.generarRespuesta(1));
                } else {
                    System.out.println("[HILO SERVIDOR " + this.name + "] Contraseña incorrecta");
                    output.writeUTF(this.generarRespuesta(0));
                }
            } else {
                System.out.println("[HILO SERVIDOR " + this.name + "] Introducido usuario cuando se esperaba contraseña!");
                output.writeUTF(this.generarRespuesta(0));
            }
        }

    }

    public String generarRespuesta(int estado) {
        if (ServidorTh.comprobarUser == false) {
            return estado + ":usuario";
        } else if (ServidorTh.comprobarPass == false) {
            return estado + ":contraseña";
        } else {
            return estado + ":fin";
        }
    }

}
