package tools.config;

public class FPS {
    private static final Config FPS_CONFIG = Config.getConfig("fps");
    private static final int updatePersonalFirst = FPS_CONFIG.getProperty(Integer.class, "updatePersonalFirst");
    private static final int updatePersonalSecond = FPS_CONFIG.getProperty(Integer.class, "updatePersonalSecond");
    private static final int sendRequestTPS = FPS_CONFIG.getProperty(Integer.class, "sendRequestTPS");
    private static final int updateTPS = FPS_CONFIG.getProperty(Integer.class, "updateTPS");

    public static int updatePersonalFirst() {
        return updatePersonalFirst;
    }

    public static int updatePersonalSecond() {
        return updatePersonalSecond;
    }

    public static int sendRequestTPS() {
        return sendRequestTPS;

    }

    public static int updateTPS() {
        return updateTPS;
    }

}
