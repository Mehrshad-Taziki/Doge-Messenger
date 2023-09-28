package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.Request;
import responses.Response;
import tools.gson.Deserializer;
import tools.gson.Serializer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketResponseSender implements ResponseSender {
    private static final Logger logger = LogManager.getLogger(SocketResponseSender.class);
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final Gson gson;

    public SocketResponseSender(Socket socket) throws IOException {
        this.socket = socket;
        this.scanner = new Scanner(socket.getInputStream());
        this.printStream = new PrintStream(socket.getOutputStream());
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Request.class, new Deserializer<>())
                .registerTypeAdapter(Response.class, new Serializer<>())
                .create();
    }

    @Override
    public Request getRequest() {
        String request = scanner.nextLine();
        return gson.fromJson(request, Request.class);
    }

    @Override
    public void sendResponse(Response response) {
        printStream.println(gson.toJson(response, Response.class));
    }

    @Override
    public void close() {
        try {
            printStream.close();
            scanner.close();
            socket.close();
        } catch (IOException e) {
            logger.error("Closing Socket Response Sender Failed " + e.getMessage());
        }
    }
}
