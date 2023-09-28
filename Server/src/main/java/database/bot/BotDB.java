package database.bot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class BotDB {
    private static final Logger logger = LogManager.getLogger(BotDB.class);
    private static final Object syncObj = new Object();
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final Config config = Config.getConfig("bot");
    private final static File botFile = new File(config.getProperty(String.class, "botDirectory"));
    private final static File botAddresses = new File(config.getProperty(String.class, "botAddress"));

    public static Object getSyncObj() {
        return syncObj;
    }

    public static synchronized String getBotData(int id) {
        try {
            if (id == -1) return null;
            String data = objectMapper.readValue(new File(botFile.getPath() + "/" + id + ".json"), String.class);
            logger.info("Loaded Bot " + id + " Data");
            return data;
        } catch (IOException e) {
            logger.error("Unable To Load Bot " + id + " Data");
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized void saveNewBotData(int id) {
        try {
            objectMapper.writeValue(new FileWriter(botFile.getPath() + "/" + id + ".json"), null);
            logger.info("Created Bot " + id + " File");
        } catch (IOException e) {
            logger.error("Unable To Create Bot " + id + " File");
            e.printStackTrace();
        }
    }

    public static synchronized void updateBotData(int id, String data) {
        try {
            objectMapper.writeValue(new FileWriter(botFile.getPath() + "/" + id + ".json"), data);
            logger.info("Updated Bot " + id + " Data");
        } catch (IOException e) {
            logger.error("Unable To Update Bot " + id + " Data");
            e.printStackTrace();
        }
    }

    public static synchronized HashMap<Integer, String> getBotAddresses() {
        try {
            if (botAddresses.createNewFile()) {
                return new HashMap<>();
            } else {
                HashMap<Integer, String> map = objectMapper.readValue(botAddresses, new TypeReference<HashMap<Integer, String>>() {
                });
                logger.info("loaded Addresses map");
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("unable to get Addresses Map");
            return new HashMap<>();
        }
    }

    public static synchronized void addBotAddresses(int id, String path) {
        HashMap<Integer, String> botMap = getBotAddresses();
        botMap.put(id, path);
        try {
            objectMapper.writeValue(new FileWriter(botAddresses), botMap);
            logger.info("updated Addresses Map");
        } catch (IOException e) {
            logger.error("unable to update Addresses Map");
            e.printStackTrace();
        }
    }
}
