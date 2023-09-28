package model;

import java.util.Objects;

public class Notification {

    private final String text;
    private final int type;
    private final String username;


    public Notification(String text, int type, String username) {
        this.text = text;
        this.type = type;
        this.username = username;
    }

    public static Notification type1(String username) {
        return new Notification("Declined your follow request", 1, username);
    }

    public static Notification type2(String username) {
        return new Notification("Stopped following you", 2, username);
    }

    public static Notification type3(String username) {
        return new Notification("Has requested to follow you", 3, username);
    }

    public static Notification type4(String username) {
        return new Notification("Accepted your follow request", 4, username);
    }

    public static Notification type5(String username) {
        return new Notification("Started following you", 5, username);
    }

    public static Notification generateNotification(int type, String username) {
        switch (type){
            case 1 -> {
                return Notification.type1(username);
            }
            case 2 -> {
                return Notification.type2(username);
            }
            case 3 -> {
                return Notification.type3(username);
            }
            case 4 -> {
                return Notification.type4(username);
            }
            default -> {
                return Notification.type5(username);
            }
        }
    }

    public int getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return username + " " + text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return type == that.type &&
                Objects.equals(text, that.text) &&
                Objects.equals(username, that.username);
    }

}
