import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import httphandlers.*;
import managing.FileBackedTaskManager;
import managing.Managers;
import managing.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer server;
    private static TaskManager manager = Managers.createTaskManager();

    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/task", new TaskHttpHandler(manager));
        server.createContext("/epic", new EpicHttpHandler(manager));
        server.createContext("/subtask", new SubtaskHttpHandler(manager));
        server.createContext("/history", new HistoryHttpHandler(manager));
        server.createContext("/prioritized", new PriorityHttpHandler(manager));

        server.start();
    }
}
