package database;

import config.Config;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Images {
    private static final Config IMAGES_CONFIG = Config.getConfig("imageDB");
    private static final Logger logger = LogManager.getLogger(Images.class);


    public static Image get(int id) {
        try {
            String images = IMAGES_CONFIG.getProperty(String.class, "imageDB");
            File imagesDB = new File(images, id + ".png");
            BufferedImage image = ImageIO.read(imagesDB);
            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            logger.error("Requested A None Exiting Image ");
        }
        return get(0);
    }

    public static void save(Image image) {
        try {
            int id = ID.generateNewID();
            String images = IMAGES_CONFIG.getProperty(String.class, "imageDB");
            File imagesDB = new File(images, id + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imagesDB);
        } catch (Exception e) {
            logger.error("Saving Image Failed ");
        }
    }

}
