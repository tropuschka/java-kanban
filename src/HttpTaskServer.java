import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import httphandlers.*;
import managing.Managers;
import managing.TaskManager;

import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer server;
    private static final TaskManager manager = Managers.createTaskManager();


    public static void main(String[] args) throws Exception {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new BaseHttpHandler());
        server.createContext("/stop", new StopHandler());
        server.createContext("/task", new TaskHttpHandler(manager));
        server.createContext("/epic", new EpicHttpHandler(manager));
        server.createContext("/subtask", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/priority", new PriorityHttpHandler(manager));
        server.start();
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
}