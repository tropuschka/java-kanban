import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import httphandlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/task", new TaskHttpHandler());
        server.createContext("/epic", new EpicHttpHandler());
        server.createContext("/subtask", new SubtaskHttpHandler());
        server.createContext("/history", new HistoryHttpHandler());
        server.createContext("/prioritized", new PriorityHttpHandler());

        server.start();
    }
}
