package database;

import config.Config;
import database.saver.MainSaver;
import model.Post;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Objects;

public class Deleter {
    private static final Logger logger = LogManager.getLogger(User.class);
    private static final Config databaseConfig = Config.getConfig("database");

    public static void deletePost(Post post) {
        for (User user : post.getLikes()) {
            user.getLiked().remove(post);
            MainSaver.get().User.save(user);
        }
        File postFile = getPostFile(post.getId());
        deleteFolder(postFile);
        logger.info(post.getId() + " File Deleted");
    }

    public static void deleteUser(User user) {
        System.out.println(user.getId() + "del User function");
        File userFile = Users.getUserFile(user.getId());
        deleteFolder(userFile);
        logger.info(user.getUsername() + "/" + user.getId() + " File Deleted");
    }

    public static void deleteGroupFile(String username, String groupName) {
        logger.info("Chat Of Group" + groupName + " File Deleted");
        String path = databaseConfig.getProperty(String.class, "users");
        path += "\\" + Users.getID(username) + "\\" + "groups" + "\\" + groupName + ".txt";
        File groupFile = new File(path);
        groupFile.delete();
    }

    public static void deleteChatFile(String user1, String user2) {
        try {
            getChatFile(user1, user2).delete();
            getChatFile(user2, user1).delete();
            logger.info("Chat Of " + user1 + "/" + Users.getID(user1) + " With " + user2 + "/" + Users.getID(user2) + " File Deleted");
        } catch (Exception e) {
            logger.error("Deleting Chat Of " + user1 + "/" + Users.getID(user1) + " With " + user2 + "/" + Users.getID(user2) + " Failed");
        }
    }

    public static void deleteFolder(File file) {
        if (file != null) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                if (subFile.isDirectory()) {
                    deleteFolder(subFile);
                } else {
                    subFile.delete();
                }
            }
            file.delete();
        }
    }

    private static File getPostFile(long postId) {
        logger.info("File Of Post With Id " + postId + " Opened");
        String path = databaseConfig.getProperty(String.class, "posts");
        File file = new File(path);
        File[] posts = file.listFiles();
        assert posts != null;
        for (File post : posts) {
            if (Integer.parseInt(post.getName()) == postId) {
                return post;
            }
        }
        return null;
    }

    private static File getChatFile(String username1, String username2) {
        logger.info("Chat Of " + username1 + '/' + Users.getID(username1) + " With " + username2 + '/' + Users.getID(username2) + " File Opened");
        String path = databaseConfig.getProperty(String.class, "users");
        path += "\\" + Users.getID(username1) + "\\" + "massage" + "\\" + username2 + ".txt";
        return new File(path);
    }
}
