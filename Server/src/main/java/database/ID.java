package database;

import java.util.Date;

public class ID {
    private static int lastGeneratedID = 0;

    public static int generateNewID() {
        Date date = new Date();
        long help = date.getTime() - 1617152000000L;
        lastGeneratedID = (int) help;
        return (int) help;
    }

    public static int getLastGeneratedID() {
        return lastGeneratedID;
    }
}
