package database.saver;

import config.Config;
import database.Users;
import model.Message;
import model.SavedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SavedMessageSaver implements Saver<SavedMessage> {
    private static final Logger logger = LogManager.getLogger(UserSaver.class);
    private static final Config databaseConfig = Config.getConfig("database");

    private static File getSavedMessageFile(String username) {
        logger.info("Saved Message Of " + username + '/' + Users.getID(username) + " File Opened");
        String path = databaseConfig.getProperty(String.class, "users");
        path += "\\" + Users.getID(username) + "\\" + "savedMessage.txt";
        return new File(path);
    }

    @Override
    public void save(SavedMessage savedMessage) {
        String user = savedMessage.getUsername();
        try {
            logger.info("SavedMessage Of " + user + '/' + Users.getID(user) + " File Saved");
            File chatFile = getSavedMessageFile(user);
            PrintStream chatWriter = new PrintStream(chatFile);
            chatWriter.println(savedMessage.getUnreadMessages());
            chatWriter.println(savedMessage.getUsername());
            for (Message message :
                    savedMessage.getMessages()) {
                chatWriter.println(message.getId());
                chatWriter.println(message.getImageID());
                chatWriter.println(message.getUser().getUsername());
                chatWriter.println(message.isDeleted());
                chatWriter.println(message.getText());
                chatWriter.println("-");
            }
            chatWriter.flush();
            chatWriter.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving SavedMessage File Of " + user + '/' + Users.getID(user) + " Failed \n" + e.getMessage());
        }
    }
}
