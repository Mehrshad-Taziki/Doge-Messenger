package config;

public class Alerts {
    private static final Config alertsConfig = Config.getConfig("alerts");

    public static String password() {
        return alertsConfig.getProperty(String.class, "password");

    }

    public static String username() {
        return alertsConfig.getProperty(String.class, "username");

    }

    public static String online() {
        return alertsConfig.getProperty(String.class, "online");

    }

    public static String passwordNS() {
        return alertsConfig.getProperty(String.class, "passwordNS");

    }

    public static String emailA() {
        return alertsConfig.getProperty(String.class, "emailAvailable");

    }

    public static String emailV() {
        return alertsConfig.getProperty(String.class, "emailValid");

    }

    public static String numberAvailable() {
        return alertsConfig.getProperty(String.class, "numberAvailable");
    }

    public static String reported(){
        return alertsConfig.getProperty(String.class, "reported");
    }
    public static String reposted(){
        return alertsConfig.getProperty(String.class, "reposted");
    }

    public static String commented() {
        return alertsConfig.getProperty(String.class, "commented");

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
