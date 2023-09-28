package tools.config;

public class Alerts {
    private static final Config alertsConfig = Config.getConfig("alerts");

    public static String emptyComment() {
        return alertsConfig.getProperty(String.class, "emptyComment");
    }

    public static String commented() {
        return alertsConfig.getProperty(String.class, "commented");

    }

    public static String lastComment() {
        return alertsConfig.getProperty(String.class, "lastComment");

    }
    public static String firstComment(){
        return alertsConfig.getProperty(String.class, "firstComment");
    }

    public static String lastPost() {
        return alertsConfig.getProperty(String.class, "lastPost");

    }

    public static String firstPost() {
        return alertsConfig.getProperty(String.class, "firstPost");

    }

    public static String noComment() {
        return alertsConfig.getProperty(String.class, "noComment");

    }

    public static String userNotExist() {
        return alertsConfig.getProperty(String.class, "userNotExist");

    }

    public static String cantAddToContacts() {
        return alertsConfig.getProperty(String.class, "cantAddToContacts");

    }

    public static String alreadyInContact() {
        return alertsConfig.getProperty(String.class, "alreadyInContact");

    }

    public static String cantAddToGroups() {
        return alertsConfig.getProperty(String.class, "cantAddToGroups");

    }

    public static String alreadyInGroup() {
        return alertsConfig.getProperty(String.class, "alreadyInGroup");

    }

    public static String emptyPost() {
        return alertsConfig.getProperty(String.class, "emptyPost");

    }

    public static String offline(){
        return alertsConfig.getProperty(String.class, "offline");
    }

    public static String posted() {
        return alertsConfig.getProperty(String.class, "posted");

    }

    public static String cantSendMessage() {
        return alertsConfig.getProperty(String.class, "cantSendMessage");

    }

    public static String repostOwnPost() {
        return alertsConfig.getProperty(String.class, "repostOwnPost");

    }

    public static String repostReposted() {
        return alertsConfig.getProperty(String.class, "repostReposted");

    }

    public static String alreadyReported() {
        return alertsConfig.getProperty(String.class, "alreadyReported");

    }

    public static String groupAlreadyExist() {
        return alertsConfig.getProperty(String.class, "groupAlreadyExist");
    }

    public static String contactAlreadyExist() {
        return alertsConfig.getProperty(String.class, "contactAlreadyExist");
    }
}
