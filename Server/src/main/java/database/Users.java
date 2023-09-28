package database;

import config.Config;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Users {
    private static final Logger logger = LogManager.getLogger(Users.class);
    private static final Config databaseConfig = Config.getConfig("database");
    private static final String usersDbPath = databaseConfig.getProperty(String.class, "usersDB");
    private static final Map<Integer, String> allUsers = allUsers();

    public Users() {
    }

    private static Map<Integer, String> allUsers() {
        Map<Integer, String> allUsers = new HashMap<>();
        File dbFile = new File(usersDbPath);
        try {
            Scanner scanner = new Scanner(dbFile);
            scanner.next();
            int usersCount = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < usersCount; i++) {
                int id = scanner.nextInt();
                String username = scanner.next();
                allUsers.put(id, username);
            }
            logger.info("UsersDB File Loaded");
            return allUsers;
        } catch (FileNotFoundException e) {
            logger.error("File Not Found : FileNotFoundException");
            return allUsers;
        }
    }

    public static boolean userExists(String username) {
        return allUsers.containsValue(username);
    }

    public static int getID(String username) {
        for (Map.Entry<Integer, String> user :
                allUsers.entrySet()) {
            if (user.getValue().equals(username)) {
                return user.getKey();
            }
        }
        return 0;
    }

    public static File getUserFile(String userName) {
        logger.info(userName + '/' + Users.getID(userName) + " File Opened");
        int id = getID(userName);
        String path = databaseConfig.getProperty(String.class, "users");
        File file = new File(path);
        File[] users = file.listFiles();
        assert users != null;
        for (File user : users) {
            if (user.getName().equals(String.valueOf(id))) {
                return user;
            }
        }
        return null;
    }

    public static File getUserFile(int id) {
        String path = databaseConfig.getProperty(String.class, "users");
        File file = new File(path);
        File[] users = file.listFiles();
        assert users != null;
        for (File user : users) {
            if (user.getName().equals(String.valueOf(id))) {
                return user;
            }
        }
        return null;
    }

    public static boolean ifFollowed(User client, String username) {
        for (User user :
                client.getFollowings()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifFollowingYou(User client, String username) {
        for (User user :
                client.getFollowers()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifBlocked(User client, String userName) {
        for (User user :
                client.getBlocked()) {
            if (user.getUsername().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifMuted(User client, String userName) {
        for (User user :
                client.getMuted()) {
            if (user.getUsername().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void add(int id, String username) {
        allUsers.put(id, username);
        logger.info(username + '/' + id + " Added To DataBase");
    }

    public void save() {
        try {
            PrintStream printStream = new PrintStream(usersDbPath);
            printStream.println("[ " + allUsers.size() + " ]:");
            for (Map.Entry<Integer, String> user :
                    allUsers.entrySet()) {
                printStream.println(user.getKey() + " " + user.getValue());
            }
            logger.info("UsersDB File Saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("File Not Fount : FileNotFoundException");
        }
    }

    public void delete(int id) {
        logger.info("User With Id " + id + " Deleted From DataBase");
        allUsers.remove(id);
    }

    public Collection<String> getUsers() {
        return allUsers.values();
    }

}
