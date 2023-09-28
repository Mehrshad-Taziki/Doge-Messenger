package tools.config;

public class Sizes {
    private static final Config SIZES_CONFIG = Config.getConfig("sizes");

    public static int commentLabel(){
        return SIZES_CONFIG.getProperty(Integer.class, "commentTextLabel");
    }
    public static int motherLabel(){
        return SIZES_CONFIG.getProperty(Integer.class, "motherPostTextLabel");
    }
    public static int contactMessageArea(){
        return SIZES_CONFIG.getProperty(Integer.class, "contactMessageArea");
    }
    public static int groupMessageArea(){
        return SIZES_CONFIG.getProperty(Integer.class, "groupMessageArea");
    }
    public static int savedMessage(){
        return SIZES_CONFIG.getProperty(Integer.class, "savedMessage");
    }
    public static int privateChat(){
        return SIZES_CONFIG.getProperty(Integer.class, "privateChat");
    }
    public static int message(){
        return SIZES_CONFIG.getProperty(Integer.class, "message");
    }
}
