package database.saver;

import config.Config;
import database.Users;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class PrivateChatSaver implements Saver<PrivateChat> {
    private static final Logger logger = LogManager.getLogger(PrivateChatSaver.class);
    private static final Config databaseConfig = Config.getConfig("database");

    private static File getChatFile(String username1, String username2) {
        logger.info("Chat Of " + username1 + '/' + Users.getID(username1) + " With " + username2 + '/' + Users.getID(username2) + " File Opened");
        String path = databaseConfig.getProperty(String.class, "users");
        path += "\\" + Users.getID(username1) + "\\" + "massage" + "\\" + username2 + ".txt";
        return new File(path);
    }

    @Override
    public void save(PrivateChat privateChat) {
        String user1 = privateChat.getViewerUser().getUsername();
        String user2 = privateChat.getSecondUser().getUsername();
        try {
            logger.info("Chat Of " + user1 + '/' + Users.getID(user1) + " With "
                    + user2 + '/' + Users.getID(user2) + " File Saved");
            File chatFile = getChatFile(user1, user2);
            PrintStream chatWriter = new PrintStream(chatFile);
            chatWriter.println(privateChat.getUnreadMessages());
            chatWriter.println(privateChat.getSecondUser().getUsername());
            for (Message message :
                    privateChat.getMessages()) {
                chatWriter.println(message.getId());
                chatWriter.println(message.getImageID());
                chatWriter.println(message.getStatus());
                chatWriter.println(message.getUser().getUsername());
                chatWriter.println(message.isDeleted());
                chatWriter.println(message.getText());
                chatWriter.println("-");
            }
            chatWriter.flush();
            chatWriter.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Chat File Of " + user1 + '/' + Users.getID(user1) + " With "
                    + user2 + '/' + Users.getID(user2) + " Failed \n" + e.getMessage());
        }
    }
}
