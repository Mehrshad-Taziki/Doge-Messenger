package tools.config;


public class FrameStats {
    private static final Config FRAME_CONFIG = Config.getConfig("frame");
    private static final String title = FRAME_CONFIG.getProperty(String.class, "title");
    private static final boolean resizable = FRAME_CONFIG.getProperty(Boolean.class, "resizable");
    private static final int height = FRAME_CONFIG.getProperty(Integer.class, "height");
    private static final int width = FRAME_CONFIG.getProperty(Integer.class, "width");
    private static final String image = FRAME_CONFIG.getProperty(String.class, "logo");

    public static String title() {
        return title;
    }

    public static boolean resizable() {
        return resizable;
    }

    public static int height() {
        return height;
    }

    public static int width() {
        return width;
    }

    public static String image() {
        return image;
    }
}
