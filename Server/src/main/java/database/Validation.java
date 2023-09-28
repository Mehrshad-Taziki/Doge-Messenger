package database;

import config.Config;
import database.DBSet.Context;
import model.User;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static final Config DATABASE_CONFIG = Config.getConfig("database");

    public static boolean correctUser(String userName, String passWord) {
        System.out.println("nigga1");
        User user = Context.get().Users.get(userName);
        System.out.println("nigga2");
        if (user != null) {
            return user.getPassword().equals(passWord);
        } else return false;
    }

    public static boolean isValidPass(String password) {
        return password.length() >= 8;
    }

    public static boolean isValidEmail(String email) {
        final String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isAvailableUser(String userName) {
        return !Users.userExists(userName);
    }

    public static boolean isAvailableNumber(String phoneNumber) {
        try {
            String path = DATABASE_CONFIG.getProperty(String.class, "users");
            File file = new File(path);
            File[] users = file.listFiles();
            assert users != null;
            for (File user : users) {
                path = user.getPath() + "\\" + "info.txt";
                File userInfo = new File(path);
                Scanner sc = new Scanner(userInfo);
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                if (sc.next().equals(phoneNumber)) {
                    return false;
                }
                sc.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAvailableEMail(String email) {
        try {
            String path = DATABASE_CONFIG.getProperty(String.class, "users");
            File file = new File(path);
            File[] users = file.listFiles();
            assert users != null;
            for (File user : users) {
                path = user.getPath() + "\\" + "info.txt";
                File userInfo = new File(path);
                Scanner sc = new Scanner(userInfo);
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                sc.next();
                if (sc.next().equals(email)) {
                    return false;
                }
                sc.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
