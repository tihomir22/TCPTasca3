/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcptasca3.threads;

import tcptasca3.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sportak
 */
public class Cliente {

    final static String HOST = "localhost";
    static Scanner teclado = new Scanner(System.in);
    static String mensaje = " ";
    //Puerto del servidor
    final static int PUERTO = 5000;
    static String prefijo = "usuario";
    static DataInputStream in;
    static DataOutputStream out;
    static boolean boolUsuario = false;
    static boolean boolPass = false;
    static String usuarioRes = "";
    static String passRes = "";
    static String mensajeEnviado = "";

    public static void main(String[] args) {

        try {
            Socket sc = new Socket(HOST, PUERTO);
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            while (!prefijo.equalsIgnoreCase("fin")) {
                System.out.println("[CLIENTE] Voy a enviar un mensaje...");
                mensajeEnviado = teclado.nextLine();
                out.writeUTF(prefijo + ":" + mensajeEnviado);
                Thread.sleep(1000);
                System.out.println("[CLIENTE] Esperando respuesta...");
                mensaje = in.readUTF();

                System.out.println("[CLIENTE] Necesito enviara ...  " + mensaje);
                prefijo = mensaje;
                procesarRespuesta(mensaje);

            }
            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void procesarRespuesta(String mensaje) {
        String[] respuestaPartida = mensaje.split(":");
        if (respuestaPartida[0].equalsIgnoreCase("0") && respuestaPartida[1].equalsIgnoreCase("usuario")) {
            System.out.println("ERROR USUARIO INCORRECTO");
        } else if (respuestaPartida[0].equalsIgnoreCase("1") && respuestaPartida[1].equalsIgnoreCase("usuario")) {
            System.out.println("USUARIO CORRECTO!!!");
            usuarioRes = mensajeEnviado;
        }

        if (respuestaPartida[0].equalsIgnoreCase("0") && respuestaPartida[1].equalsIgnoreCase("contraseña")) {
            System.out.println("ERROR COTNRASEÑA INCORRECTO");
        } else if (respuestaPartida[0].equalsIgnoreCase("1") && respuestaPartida[1].equalsIgnoreCase("contraseña")) {
            System.out.println("CONTRASEÑA CORRECTO!!!");
            passRes = mensajeEnviado;
        }

        if (respuestaPartida[0].equalsIgnoreCase("0") && respuestaPartida[1].equalsIgnoreCase("fin")) {
            System.out.println("Usuario " + usuarioRes + "logeado correctamente con contraseña " + passRes);
        }

    }
}
