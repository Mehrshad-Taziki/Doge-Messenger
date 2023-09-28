package bot;

import database.ID;
import database.Users;
import database.bot.BotDB;
import database.saver.MainSaver;
import model.DateTime;
import model.User;
import network.ConnectionHub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class BotStarter {
    private static final Logger logger = LogManager.getLogger(BotStarter.class);
    private final ConnectionHub hub;

    public BotStarter(ConnectionHub hub) {
        this.hub = hub;
    }

    public void initializeAll() {
        HashMap<Integer, String> botAddresses = BotDB.getBotAddresses();
        for (int id :
                botAddresses.keySet()) {
            initializeOne(id, botAddresses.get(id));
        }
        logger.info("All Registered Bots Started");
    }

    public void initializeOne(int id, String path) {
        synchronized (hub.getOnlineUsers()) {
            hub.getOnlineUsers().add(String.valueOf(id));
        }
        new BotHandler(id, path).start();
    }

    public boolean addNewBot(String path) {
        System.out.println("DA");
        try {
            File file = new File(path);
            System.out.println("DA1");
            URL url = new URL("file:///" + file.getParentFile().getAbsolutePath() + "/");
            System.out.println("DA2");
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            System.out.println("DA3");
            Class<?> botClass = classLoader.loadClass(file.getName().substring(0, file.getName().lastIndexOf(".")));
            System.out.println("DA4");
            checkClassValidity(botClass);
            System.out.println("DA5");
            add(file);
        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException e) {
            logger.error("Adding New Bot Failed From Address : " + path);
            return false;
        }
        return true;
    }

    private void checkClassValidity(Class<?> botClass) throws NoSuchMethodException {
        botClass.getMethod("getFields");
        botClass.getMethod("setFields", String.class);
        botClass.getMethod("answerMessage", String.class, int.class);
        botClass.getMethod("answerGPMessage", String.class, int.class, int.class);
        botClass.getMethod("sendMessage", int.class);
        botClass.getMethod("sendGPMessage", int.class);
        botClass.getMethod("sendComment", String.class, int.class, int.class);
        botClass.getMethod("sendTweet", String.class);
        botClass.getMethod("invokeAnswerMessage");
        botClass.getMethod("invokeAnswerGroupMessage");
        botClass.getMethod("invokeSendMessage");
        botClass.getMethod("invokeSendGroupMessage");
        botClass.getMethod("invokeSendComment");
        botClass.getMethod("invokeSendTweet");
        botClass.getMethod("acceptInvite");
    }

    private void add(File file) {
        System.out.println("Da2");
        String username = file.getName().substring(0, file.getName().lastIndexOf("."));
        int id;
        synchronized (BotDB.getSyncObj()) {
            while (Users.userExists(username)) username = "-" + username;
            id = ID.generateNewID();
            User.UserBuilder builder = new User.UserBuilder();
            builder.setId(id);
            builder.setUserName(username);
            builder.setName(username);
            builder.setLastName("Bot");
            builder.setBirthDay(new DateTime(0, 0, 0, 0, 0));
            builder.setEmail("Email");
            builder.setBiography("-");
            builder.setPassWord("Password");
            builder.setPhoneNumber("123321");
            builder.setImageID(0);
            User user = builder.build();
            BotDB.saveNewBotData(id);
            ConnectionHub.getAllUsers().add(user.getId(), user.getUsername());
            ConnectionHub.getAllUsers().save();
            MainSaver.get().User.save(user);
            BotDB.addBotAddresses(id, file.getAbsolutePath());
            logger.info("Bot " + username + " File Created");
        }
        initializeOne(id, file.getAbsolutePath());
    }
}
