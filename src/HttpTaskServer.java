import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import httphandlers.BaseHttpHandler;
import httphandlers.TaskHttpHandler;
import managing.Managers;
import managing.TaskManager;

import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.net.Socket;

public class HttpTaskServer {
    private static HttpServer server;
    private static TaskManager manager = Managers.createTaskManager();


    public static void main(String[] args) throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new BaseHttpHandler());
        server.createContext("/stop", new StopHandler());
        server.createContext("/task", new TaskHttpHandler(manager));
        server.setExecutor(null); // creates a default executor
        server.start();


//        if (hostAvailabilityCheck()) System.out.println("Server run");
//        else System.out.println("It's dead");

        // server.stop(100000000);


    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Hello, Galya!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class StopHandler implements HttpHandler { //странно работает, но работает
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Stop!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            server.stop(100);
        }
    }

/*    public static boolean hostAvailabilityCheck() {
        try (Socket s = new Socket(String.valueOf(server.getAddress()), 8000)) {
            return true;
        } catch (IOException ex) {
            /* ignore
        }
        return false;
    } */

}