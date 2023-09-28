package network;

import bot.BotStarter;
import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class BotAdder extends Thread {
    private static final Logger logger = LogManager.getLogger(BotAdder.class);
    private static final Config config = Config.getConfig("bot");
    private final BotStarter botStarter;


    public BotAdder(BotStarter botStarter) {
        this.botStarter = botStarter;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String addString = config.getProperty(String.class, "addBot");
                String command = scanner.nextLine();
                if (command.startsWith(addString + " ") && !command.substring(addString.length()).isBlank())
                    botStarter.addNewBot(command.substring(addString.length() + 1));
                else
                    logger.warn("Wrong Command Entered");
            } catch (Exception ignored) {
            }
        }
    }
}
