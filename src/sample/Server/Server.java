package sample.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            while (true){
                System.out.println("Waiting for a new client!");
                Socket s = ss.accept();
                System.out.println("Client connected!");

                ServerRunnable sr = new ServerRunnable(s);
                sr.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
