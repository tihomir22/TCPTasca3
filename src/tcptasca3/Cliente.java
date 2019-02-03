/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcptasca3;

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
public class Cliente extends Thread {

    //Host del servidor o direccion ip
    final String HOST = "localhost";
    Scanner teclado = new Scanner(System.in);
    String mensaje = " ";
    //Puerto del servidor
    final int PUERTO = 5000;
    String prefijo = "usuario";
    DataInputStream in;
    DataOutputStream out;

    @Override
    public void run() {
        try {
            Socket sc = new Socket(this.HOST, this.PUERTO);
            this.in = new DataInputStream(sc.getInputStream());
            this.out = new DataOutputStream(sc.getOutputStream());

            while (!this.prefijo.equalsIgnoreCase("fin")) {
                System.out.println("[CLIENTE] Voy a enviar un mensaje...");
                this.out.writeUTF(prefijo + ":" + teclado.nextLine());
                System.out.println("[CLIENTE] Esperando respuesta...");
                mensaje = this.in.readUTF();
                System.out.println("[CLIENTE] Necesito enviara ...  " + mensaje);
                this.prefijo = mensaje;
            }

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
