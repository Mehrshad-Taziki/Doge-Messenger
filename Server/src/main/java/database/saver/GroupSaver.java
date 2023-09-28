package database.saver;

import config.Config;
import database.Users;
import model.Group;
import model.Message;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashSet;

public class GroupSaver implements Saver<Group> {
    private static final Logger logger = LogManager.getLogger(GroupSaver.class);
    private static final Config databaseConfig = Config.getConfig("database");

    private static File getGroupFile(String viewer, String groupName) {
        logger.info("Chat Of Group" + groupName + " File Opened");
        String path = databaseConfig.getProperty(String.class, "users");
        path += "\\" + Users.getID(viewer) + "\\" + "groups" + "\\" + groupName + ".txt";
        return new File(path);
    }

    @Override
    public void save(Group group) {
        String viewer = group.getViewerUser().getUsername();
        HashSet<User> otherUsers = group.getUsers();
        try {
            logger.info("Chat Of Group" + group.getGroupName() + " File Saved");
            File chatFile = getGroupFile(viewer, group.getGroupName());
            PrintStream chatWriter = new PrintStream(chatFile);
            chatWriter.println(group.getID());
            chatWriter.println(group.getUnreadMessages());
            chatWriter.println(group.getGroupName());
            chatWriter.println("Members[ " + otherUsers.size() + " ]");
            otherUsers.forEach(user -> chatWriter.println(user.getUsername()));
            for (Message message :
                    group.getMessages()) {
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
            logger.warn("Saving Chat File Of Group :" + group.getGroupName() + " Failed \n" + e.getMessage());
        }
    }
}
