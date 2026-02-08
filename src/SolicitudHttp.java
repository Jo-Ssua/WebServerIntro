import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class SolicitudHttp implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;

    // Constructor
    public SolicitudHttp(Socket socket) throws Exception
    {
        this.socket = socket;
    }

    public void run() {
        try {
            proceseSolicitud();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void proceseSolicitud() throws Exception {
        //goku fase 1
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        //goku fase 4
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());



        String linea = "";
        String firstLine = "";

        System.out.println("========START OF LINE===========");

        //print line
        while ((linea = in.readLine()) != null && !linea.isEmpty()) {
            if(firstLine.isEmpty()) {
                firstLine = linea;
            }
            System.out.println(linea);
        }

        System.out.println("========END OF LINE===========\n\n");

        // Extrae el nombre del archivo de la línea de solicitud.
        StringTokenizer partesLinea = new StringTokenizer(firstLine);
        String method = partesLinea.nextToken();
        String nombreArchivo = partesLinea.nextToken();
        nombreArchivo = "www" + nombreArchivo;


        String lineaDeEstado = null;
        String lineaHeader = null;
        String cuerpoMensaje = null;

        if ( new File(nombreArchivo).exists() ) {
            lineaDeEstado = "HTTP/1.0 200 OK" + CRLF;
            lineaHeader = "Content-type: " + contentType(nombreArchivo) + CRLF;
            // Enviar línea de estado
            enviarString(lineaDeEstado, out);
            // Enviar header
            enviarString(lineaHeader, out);
            enviarString(CRLF, out); //Linea en blanco


            // Enviar archivo

            FileInputStream toSend = new FileInputStream(nombreArchivo);
            enviarBytes(toSend, out);
            toSend.close();

        } else {
            lineaDeEstado = "HTTP/1.0 404 Not Found\r\n";
            lineaHeader ="Content-type: text/html" + CRLF;
            // Enviar línea de estado
            enviarString(lineaDeEstado, out);
            // Enviar header
            enviarString(lineaHeader, out);
            enviarString(CRLF, out); //Linea en blanco


            // Enviar archivo 404.html
            FileInputStream toSend = new FileInputStream("www/404.html");
            enviarBytes(toSend, out);
            toSend.close();

        }
        out.flush();

        // Cierra los streams y el socket.
        out.close();
        in.close();
        socket.close();

    }

    private static String contentType(String nombreArchivo) {
        if(nombreArchivo.endsWith(".htm") || nombreArchivo.endsWith(".html")) {
            return "text/html";
        }
        if(nombreArchivo.endsWith(".jpg") ||
                nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png")) {
            return "image/jpeg";
        }
        if(nombreArchivo.endsWith(".gif")) {
            return "image/gif";
        }
        if(nombreArchivo.endsWith(".css")) {
            return "text/css";
        }
        if(nombreArchivo.endsWith(".pdf")) {
            return "application/pdf";
        }
        return "application/octet-stream";
    }

    private static void enviarString(String line, OutputStream os) throws Exception {
        os.write(line.getBytes(StandardCharsets.UTF_8));
    }

    private static void enviarBytes(InputStream fis, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }





}
