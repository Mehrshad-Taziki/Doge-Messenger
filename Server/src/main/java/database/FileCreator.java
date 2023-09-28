package database;

import config.Config;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FileCreator {
    private static final Logger logger = LogManager.getLogger(FileCreator.class);
    private static final Config databaseConfig = Config.getConfig("database");

    public static void CreateUser(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " File Created");
            String path = databaseConfig.getProperty(String.class, "users");
            path += "\\" + user.getId();
            File userFile = new File(path);
            userFile.mkdir();
            File info = new File(path + "\\" + "info.txt");
            info.createNewFile();
            File posts = new File(path + "\\" + "posts.txt");
            posts.createNewFile();
            File follower = new File(path + "\\" + "followers.txt");
            follower.createNewFile();
            File following = new File(path + "\\" + "followings.txt");
            following.createNewFile();
            File blacklist = new File(path + "\\" + "blacklist.txt");
            blacklist.createNewFile();
            File liked = new File(path + "\\" + "liked.txt");
            liked.createNewFile();
            File rePosts = new File(path + "\\" + "repost.txt");
            rePosts.createNewFile();
            File savedMessage = new File(path + "\\" + "savedMessage.txt");
            savedMessage.createNewFile();
            File contacts = new File(path + "\\" + "contacts.txt");
            contacts.createNewFile();
            File massage = new File(path + "\\" + "massage");
            massage.mkdir();
            File groups = new File(path + "\\" + "groups");
            groups.mkdir();
        } catch (IOException e) {
            logger.warn("Create File For User " + user.getUsername() + "/" + user.getId() + " Failed");
        }
    }
    public static void createGroup(String username, String groupName) {
        try {
            logger.info("Chat Of Group" + groupName + " File Created");
            String path = databaseConfig.getProperty(String.class, "users");
            String path1 = path + "\\" + Users.getID(username) + "\\" + "groups" + "\\" + groupName + ".txt";
            File chat1 = new File(path1);
            chat1.createNewFile();
        } catch (IOException e) {
            logger.error("Creating Chat File Of Group " + groupName + " Failed \n");
        }
    }
    public static void createChat(String username1, String username2) {
        try {
            logger.info("Chat Of " + username1 + '/' + Users.getID(username1) +
                    " With " + username2 + '/' + Users.getID(username2) + " File Created");
            String path = databaseConfig.getProperty(String.class, "users");
            String path1 = path + "\\" + Users.getID(username1) + "\\" + "massage" + "\\" + username2 + ".txt";
            File chat1 = new File(path1);
            chat1.createNewFile();
            String path2 = path + "\\" + Users.getID(username2) + "\\" + "massage" + "\\" + username1 + ".txt";
            File chat2 = new File(path2);
            chat2.createNewFile();
        } catch (IOException e) {
            logger.error("Creating Chat File Of " + username1 + '/' + Users.getID(username1) + " With "
                    + username2 + '/' + Users.getID(username2) + " Failed \n");
        }
    }
}
