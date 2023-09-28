package database.saver;

import config.Config;
import database.FileCreator;
import database.Users;
import model.Contact;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

public class UserSaver implements Saver<User> {
    private static final Logger logger = LogManager.getLogger(UserSaver.class);
    private static final Config databaseConfig = Config.getConfig("database");

    public static void saveBlackList(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " BlackListFile Saved");
            File userFile = Users.getUserFile(user.getId());
            File userBlackList = new File(userFile.getPath() + "\\" + "blacklist.txt");
            PrintStream printStream = new PrintStream(userBlackList);
            printStream.println("Blacklist[ " + user.getBlocked().size() + " ]");
            for (int i = 0; i < user.getBlocked().size(); i++) {
                printStream.println(user.getBlocked().get(i).getUsername());
            }
            printStream.println("Muted[ " + user.getMuted().size() + " ]");
            for (int i = 0; i < user.getMuted().size(); i++) {
                printStream.println(user.getMuted().get(i).getUsername());
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving BlackList Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }
    }

    public static void savePosts(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " PostsFile Saved");
            File userFile = Users.getUserFile(user.getId());
            File userPosts = new File(userFile.getPath() + "\\" + "posts.txt");
            PrintStream printStream = new PrintStream(userPosts);
            printStream.println("Posts[ " + user.getPosts().size() + " ]");
            for (int i = 0; i < user.getPosts().size(); i++) {
                printStream.println(user.getPosts().get(i).getId());
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Posts Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }
    }

    public static void saveLiked(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " LikedPostFile Saved");
            File userFile = Users.getUserFile(user.getId());
            File userLiked = new File(userFile.getPath() + "\\" + "liked.txt");
            PrintStream printStream = new PrintStream(userLiked);
            printStream.println("Liked[ " + user.getLiked().size() + " ]");
            for (int i = 0; i < user.getLiked().size(); i++) {
                printStream.println(user.getLiked().get(i).getId());
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving LikedPosts Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }
    }

    public static void saveFollower(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " FollowerFile Saved");
            File userFile = Users.getUserFile(user.getId());
            File userFollowers = new File(userFile.getPath() + "\\" + "followers.txt");
            PrintStream printStream = new PrintStream(userFollowers);
            printStream.println("Followers[ " + user.getFollowers().size() + " ]");
            for (int i = 0; i < user.getFollowers().size(); i++) {
                printStream.println(user.getFollowers().get(i).getUsername());
            }
            printStream.println();
            printStream.println("Requests[ " + user.getRequests().size() + " ]");
            for (int i = 0; i < user.getRequests().size(); i++) {
                printStream.println(user.getRequests().get(i));
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Followers Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }

    }

    public static void saveFollowing(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " FollowingFile Saved");
            File userFile = Users.getUserFile(user.getId());
            File userFollowings = new File(userFile.getPath() + "\\" + "followings.txt");
            PrintStream printStream = new PrintStream(userFollowings);
            printStream.println("Followings[ " + user.getFollowings().size() + " ]");
            for (int i = 0; i < user.getFollowings().size(); i++) {
                printStream.println(user.getFollowings().get(i).getUsername());
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Following Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }
    }

    public static void saveContacts(User user) {
        try {
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " Contacts Saved");
            String path = databaseConfig.getProperty(String.class, "users");
            path += "\\" + user.getId() + "\\" + "contacts.txt";
            File groups = new File(path);
            PrintStream printStream = new PrintStream(groups);
            printStream.println("Contacts[ " + user.getContacts().size() + " ]");
            for (Map.Entry<String, Contact> contact :
                    user.getContacts().entrySet()) {
                printStream.println(contact.getKey() + " " + contact.getValue().getMembers().size());
                for (User member :
                        contact.getValue().getMembers()) {
                    printStream.println(member.getUsername());
                }
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Groups Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }
    }

    public static void saveInfo(User user) {
        File userFile = Users.getUserFile(user.getId());
        if (userFile == null) {
            FileCreator.CreateUser(user);
            saveFollower(user);
            saveFollowing(user);
            saveBlackList(user);
            savePosts(user);
            saveLiked(user);
            saveContacts(user);
            MainSaver.get().SavedMessage.save(user.getSavedMessage());
        }
        try {
            userFile = Users.getUserFile(user.getId());
            logger.info("File Of User " + user.getUsername() + '/' + user.getId() + " File Saved");
            File info = new File(userFile.getPath() + "\\" + "info.txt");
            PrintStream printStream = new PrintStream(info);
            printStream.println("ImageId: " + user.getImageID());
            printStream.println();
            printStream.println("LastSeenType: " + user.getLastSeenType());
            printStream.println();
            printStream.println("Active: " + user.isActive());
            printStream.println();
            printStream.println("Public: " + user.isPublic());
            printStream.println();
            printStream.println("ID: " + user.getId());
            printStream.println();
            printStream.println("UserName: " + user.getUsername());
            printStream.println();
            printStream.println("FullName: " + user.getName() + " " + user.getLastName());
            printStream.println();
            printStream.println(user.getBirthDay().toString());
            printStream.println();
            printStream.println("Email: " + user.getEmail());
            printStream.println();
            printStream.println("PhoneNumber: " + user.getPhoneNumber());
            printStream.println();
            printStream.println("Password: " + user.getPassword());
            printStream.println();
            printStream.println("Biography: ");
            printStream.println(user.getBiography());
            printStream.println();
            printStream.println(user.getLastSeen().toString());
            printStream.println();
            printStream.println("Public-Email: " + user.isPublicEmail());
            printStream.println("Public-Number: " + user.isPublicNumber());
            printStream.println("Public-Birthday: " + user.isPublicBirthday());
            printStream.println("Notifications[ " + user.getNotification().size() + " ]");
            for (int i = 0; i < user.getNotification().size(); i++) {
                printStream.println(user.getNotification().get(i).getType() + " " + user.getNotification().get(i).getUsername());
            }
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            logger.error("Saving Info Of " + user.getUsername() + " Failed \n" + e.getMessage());
        }
    }

    @Override
    public void save(User user) {
        if (user != null) {
            try {
                saveInfo(user);
                saveFollower(user);
                saveFollowing(user);
                saveBlackList(user);
                savePosts(user);
                saveLiked(user);
                saveContacts(user);
                MainSaver.get().SavedMessage.save(user.getSavedMessage());
                user.getPrivateChats().values().forEach(privateChat -> MainSaver.get().PrivateChat.save(privateChat));
                user.getGroups().values().forEach(group -> MainSaver.get().Group.save(group));
            } catch (Throwable e) {
                logger.error("Saving " + user.getUsername() + " Failed \n" + e.getMessage());
            }
        }
    }
}
