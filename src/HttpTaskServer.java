import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import httphandlers.BaseHttpHandler;
import httphandlers.TaskHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/task", new TaskHttpHandler());
        server.createContext("/epic", new BaseHttpHandler()); //Заменить обработчик
        server.createContext("/subtask", new BaseHttpHandler()); //Заменить обработчик
        server.createContext("/history", new BaseHttpHandler()); //Заменить обработчик
        server.createContext("/prioritized", new BaseHttpHandler()); //Заменить обработчик

        server.start();
    }
}
