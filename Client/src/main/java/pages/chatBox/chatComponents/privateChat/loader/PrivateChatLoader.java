package pages.chatBox.chatComponents.privateChat.loader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import listener.RequestListener;
import logics.LogicalAgent;
import model.DB.DBMessage;
import model.DB.DBPrivateChat;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.chatBox.chatComponents.message.loader.MessageLoader;
import pages.chatBox.chatComponents.privateChat.controller.PrivateChatController;
import tools.config.Config;

import java.io.IOException;
import java.util.ArrayList;

public class PrivateChatLoader {
    private static final Logger logger = LogManager.getLogger(PrivateChatLoader.class);
    private final Config MESSAGE_CONFIG = Config.getConfig("Message");
    private final LogicalAgent logicalAgent;
    private final PrivateChatController controller;
    private final DBUser viewer;
    private final DBPrivateChat privateChat;

    public PrivateChatLoader(LogicalAgent logicalAgent, DBPrivateChat privateChat, DBUser viewer, PrivateChatController controller) {
        this.logicalAgent = logicalAgent;
        this.privateChat = privateChat;
        this.controller = controller;
        this.viewer = viewer;
        loadMessageListener();
        loadMessagePane();
    }


    private void loadMessageListener() {
        controller.setMessageListener(new RequestListener(logicalAgent));
    }


    private void loadMessagePane() {
        ArrayList<AnchorPane> messages = new ArrayList<>();
        for (DBMessage message :
                privateChat.getMessages()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(MESSAGE_CONFIG.getProperty(String.class, "MessageComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        new MessageLoader(logicalAgent, message, viewer, fxmlLoader.getController(), privateChat);
                        messages.add(anchorPane);
                    } catch (IOException e) {
                        logger.error("Loading Message " + message.getId() + " Failed");
                    }
                }
            });
        }
        controller.build(messages);
    }

    public void updatePrivateChat(ArrayList<DBMessage> messages) {
        if (hasNewMessage(messages)) {
            this.privateChat.setMessages(messages);
            loadMessagePane();
        }
    }

    public boolean hasNewMessage(ArrayList<DBMessage> messages) {
        if (messages.size() == this.privateChat.getMessages().size()) {
            for (int i = 0; i < privateChat.getMessages().size(); i++) {
                if (!privateChat.getMessages().get(i).getText().equals(messages.get(i).getText())) return true;
                if (!privateChat.getMessages().get(i).getStatus().equals(messages.get(i).getStatus())) return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
