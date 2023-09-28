package bot;

import database.DBSet.Context;
import database.ID;
import database.bot.BotDB;
import enums.SeenStatus;
import model.Group;
import model.Message;
import model.PrivateChat;
import model.User;
import network.handler.ChatBoxHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class BotHandler extends Thread {
    private static final Logger logger = LogManager.getLogger(BotHandler.class);
    private final int id;
    private final String path;
    private final String className;
    private Class<?> botClass;

    public BotHandler(int id, String path) {
        File file = new File(path);
        this.id = id;
        this.path = "file:///" + file.getParentFile().getAbsolutePath() + "/";
        this.className = file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    public void run() {
        while (true) {
            try {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                botClass = new URLClassLoader(new URL[]{new URL(path)}).loadClass(className);
                botClass.getMethod("setFields", String.class).invoke(null, BotDB.getBotData(id));
                answerMessage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                answerGPMessage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                sendMessage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                sendGPMessage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                sendComment();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                sendTweet();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                acceptInvite();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                BotDB.updateBotData(id, (String) botClass.getMethod("getFields").invoke(null));
            } catch (IOException | ReflectiveOperationException e) {
                logger.error("Doing Bot : " + id + " Tasks Failed");
            }
        }
    }

    private void acceptInvite() {

    }

    private void sendTweet() {
        //
    }

    private void sendComment() {
        //
    }

    private void sendGPMessage() throws ReflectiveOperationException {
        if ((boolean) botClass.getMethod("invokeSendGroupMessage").invoke(null)) {
            Method method = botClass.getMethod("sendGPMessage", int.class);
            synchronized (BotDB.getSyncObj()) {
                User user = Context.get().Users.get(String.valueOf(id));
                for (int groupID : user.getGroups().keySet()) {
                    String ans = (String) method.invoke(null, groupID);
                    if (ans == null) continue;
                    if (ans.equals("")) continue;
                    Group group = user.getGroups().get(groupID);
                    ChatBoxHandler.messageGroup(ans, user, group.getGroupName(), "");
                }
            }
        }
    }

    private void sendMessage() throws ReflectiveOperationException {
        if ((boolean) botClass.getMethod("invokeSendMessage").invoke(null)) {
            Method method = botClass.getMethod("sendMessage", int.class);
            synchronized (BotDB.getSyncObj()) {
                User user = Context.get().Users.get(String.valueOf(id));
                if (user == null) return;
                for (PrivateChat pv :
                        user.getPrivateChats().values()) {
                    String ans = (String) method.invoke(null, pv.getSecondUser().getId());
                    if (ans == null) continue;
                    if (ans.equals("")) continue;
                    Message newMessage = new Message(user, ans, false, ID.generateNewID(), SeenStatus.SENT);
                    System.out.println("Game Sent to " + pv.getSecondUser().getUsername());
                    ChatBoxHandler.message(user, pv.getSecondUser(), newMessage);
                }
            }
        }
    }

    private void answerGPMessage() throws ReflectiveOperationException {
        if ((boolean) botClass.getMethod("invokeAnswerGroupMessage").invoke(null)) {
            Method method = botClass.getMethod("answerGPMessage", String.class, int.class, int.class);
            synchronized (BotDB.getSyncObj()) {
                User user = Context.get().Users.get(String.valueOf(id));
                if (user == null) return;
                for (Group group :
                        user.getGroups().values()) {
                    ArrayList<Message> questions = new ArrayList<>();
                    for (int i = 1; i <= group.getUnreadMessages(); i++) {
                        int size = group.getMessages().size();
                        questions.add(group.getMessages().get(size - i));
                    }
                    for (Message message :
                            questions) {
                        if (message.getUser().getId() == id) continue;
                        String answer = (String) method.invoke(null, message.getText(), message.getUser().getId(), group.getID());
                        if (answer == null) continue;
                        if (answer.equals("")) continue;
                        ChatBoxHandler.messageGroup(answer, user, group.getGroupName(), "");
                    }
                    group.setUnreadMessages(0);
                }
            }
        }
    }

    private void answerMessage() throws ReflectiveOperationException {
        if ((boolean) botClass.getMethod("invokeAnswerMessage").invoke(null)) {
            Method method = botClass.getMethod("answerMessage", String.class, int.class);
            synchronized (BotDB.getSyncObj()) {
                User user = Context.get().Users.get(String.valueOf(id));
                if (user == null) return;
                for (PrivateChat pv :
                        user.getPrivateChats().values()) {
                    ArrayList<Message> questions = new ArrayList<>();
                    for (int i = 1; i <= pv.getUnreadMessages(); i++) {
                        int size = pv.getMessages().size();
                        questions.add(pv.getMessages().get(size - i));
                    }
                    for (Message message :
                            questions) {
                        if (message.getUser().getId() == id) continue;
                        String answer = (String) method.invoke(null, message.getText(), message.getUser().getId());
                        if (answer == null) continue;
                        if (answer.equals("")) continue;
                        Message newMessage = new Message(user, answer, false, ID.generateNewID(), SeenStatus.SENT);
                        ChatBoxHandler.message(user, message.getUser(), newMessage);
                    }
                    pv.setUnreadMessages(0);
                }
            }
        }
    }
}
