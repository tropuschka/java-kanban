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
    private static TaskManager manager;

    public void HttpTaskServer() throws IOException {
        manager = Managers.createTaskManager();
        server = HttpServer.create(new InetSocketAddress(8080), 0);
    }


    public static void main(String[] args) throws Exception {
        server.createContext("/", new BaseHttpHandler());
        server.createContext("/stop", new StopHandler());
        server.createContext("/task", new TaskHttpHandler(manager));
        server.createContext("/epic", new EpicHttpHandler(manager));
        server.createContext("/subtask", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/priority", new PriorityHttpHandler(manager));
        start();
    }

    public static void start() {
        server.start();
    }

    public static void stop() {
        server.stop(60);
    }

    static class StopHandler implements HttpHandler { //странно работает, но работает
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Stop!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            stop();
        }
    }
}