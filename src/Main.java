import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {


    public static void main(String[] args) throws Exception {

        Main main = new Main();
    }


    public Main() throws Exception {

        //10 hilos
        ExecutorService pool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(5000);

        while (true) {
            System.out.println("Waiting for client on port 5000");

            Socket socket = serverSocket.accept();
            System.out.println("Connected to the server");

            SolicitudHttp solicitud = new SolicitudHttp(socket);

            // Lo mandamos a la piscina
            pool.execute(solicitud);





        }

    }

}