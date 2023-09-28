package tools.config;

public class PageAddresses {
    private static final Config scenesConfig = Config.getConfig("mainConfig");
    private static final String WELCOME_PAGE_ADDRESS = scenesConfig.getProperty("welcomePageScene");
    private static final String REGISTER_PAGE_ADDRESS = scenesConfig.getProperty("registerPageScene");
    private static final String LOGIN_PAGE_ADDRESS = scenesConfig.getProperty("loginPageScene");
    private static final String MAIN_PAGE_ADDRESS = scenesConfig.getProperty("mainPageScene");
    private static final String PERSONAL_PAGE_ADDRESS = scenesConfig.getProperty("personalPageScene");
    private static final String TIMELINE_PAGE_ADDRESS = scenesConfig.getProperty("timeLinePageScene");
    private static final String EXPLORE_PAGE_ADDRESS = scenesConfig.getProperty("explorePageScene");
    private static final String SETTING_PAGE_ADDRESS = scenesConfig.getProperty("settingPageScene");
    private static final String CHAT_BOX_PAGE_ADDRESS = scenesConfig.getProperty("chatBoxScene");

    public static String register() {
        return REGISTER_PAGE_ADDRESS;
    }

    public static String login() {
        return LOGIN_PAGE_ADDRESS;
    }

    public static String welcome() {
        return WELCOME_PAGE_ADDRESS;
    }

    public static String main() {
        return MAIN_PAGE_ADDRESS;
    }

    public static String personal() {
        return PERSONAL_PAGE_ADDRESS;
    }

    public static String explore() {
        return EXPLORE_PAGE_ADDRESS;
    }

    public static String timeline() {
        return TIMELINE_PAGE_ADDRESS;
    }

    public static String chatBox() {
        return CHAT_BOX_PAGE_ADDRESS;
    }

    public static String setting() {
        return SETTING_PAGE_ADDRESS;
    }
}
