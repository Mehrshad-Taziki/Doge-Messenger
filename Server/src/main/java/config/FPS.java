package config;

public class FPS {
    private static final Config FPS_CONFIG = Config.getConfig("fps");
    private static final int updateTPS = FPS_CONFIG.getProperty(Integer.class, "updateTPS");
    private static final int turnOffTime = FPS_CONFIG.getProperty(Integer.class, "turnOffTime");

    public static int updateTPS() {
        return updateTPS;
    }

    public static int turnOffTime() {
        return turnOffTime;

    }
}
