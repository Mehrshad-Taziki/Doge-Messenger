package database.DBSet;

import database.loader.UserLoader;
import model.User;

import java.util.LinkedList;

public class UserDBSet implements DBSet<User> {
    private static final LinkedList<User> loadedUsers = new LinkedList<>();
    private static final UserLoader userLoader = new UserLoader();

    @Override
    public User get(String username) {
        for (User user :
                Context.get().Users.getAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
            if (username.equals((String.valueOf(user.getId())))) {
                return user;
            }
        }
        return userLoader.load(username);
    }

    @Override
    public LinkedList<User> getAll() {
        return loadedUsers;
    }
}
