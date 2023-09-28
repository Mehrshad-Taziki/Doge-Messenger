package network;

import database.DBSet.Context;
import database.Users;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class ConnectionHub {
    private static final Users allUsers = new Users();
    private static final Logger logger = LogManager.getLogger(ConnectionHub.class);

    private final HashSet<String> onlineUsers = new HashSet<>();

    public static Users getAllUsers() {
        return allUsers;
    }

    public HashSet<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void load() {
        allUsers.getUsers().forEach(string -> Context.get().Users.get(string));
        logger.info("All Users Loaded");
    }

}
