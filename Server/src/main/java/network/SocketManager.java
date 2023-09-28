package network;

import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager extends Thread {
    private static final Logger logger = LogManager.getLogger(SocketManager.class);
    private static final Config CONNECTION_CONFIG = Config.getConfig("connection");
    private final ConnectionHub hub;

    public SocketManager(ConnectionHub hub) {
        this.hub = hub;
    }
    @Override
    public void run() {
        try {
            int port = CONNECTION_CONFIG.getProperty(Integer.class, "port");
            if (port == 0) port = 5555;
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(new SocketResponseSender(socket), hub).start();
            }
        } catch (IOException e) {
            logger.error("Failed To Start The Server " + e.getMessage());
        }
    }
}
