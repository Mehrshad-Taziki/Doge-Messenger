import bot.BotStarter;
import network.BotAdder;
import network.ConnectionHub;
import network.SocketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class main {
    private static final Logger logger = LogManager.getLogger(main.class);
    public static void main(String[] args) {
        logger.info("Server Started :D");
        ConnectionHub hub = new ConnectionHub();
        hub.load();
        new SocketManager(hub).start();
        BotStarter botStarter = new BotStarter(hub);
        botStarter.initializeAll();
        new BotAdder(botStarter).start();
    }
}
