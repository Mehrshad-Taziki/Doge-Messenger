package pages.chatBox.chatComponents.group.loader;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import listener.RequestListener;
import logics.LogicalAgent;
import model.DB.DBGroup;
import model.DB.DBMessage;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.chatBox.chatComponents.group.controller.GroupController;
import pages.chatBox.chatComponents.message.loader.MessageLoader;
import tools.config.Config;

import java.io.IOException;
import java.util.ArrayList;

public class GroupLoader {
    private static final Logger logger = LogManager.getLogger(GroupLoader.class);
    private final Config MESSAGE_CONFIG = Config.getConfig("Message");
    private final LogicalAgent logicalAgent;
    private final DBGroup group;
    private final GroupController controller;
    private final DBUser viewer;

    public GroupLoader(LogicalAgent logicalAgent, DBGroup group, DBUser viewer, GroupController controller) {
        this.logicalAgent = logicalAgent;
        this.group = group;
        this.controller = controller;
        this.viewer = viewer;
        loadMessageListener();
        loadMessagePane();
        loadUserListener();
        loadActionListeners();
    }


    private void loadMessageListener() {
        controller.setMessageListener(new RequestListener(logicalAgent));
    }

    private void loadUserListener() {
        controller.setUserListener(new RequestListener(logicalAgent));
    }


    private void loadActionListeners() {
        controller.setLeaveListener(new RequestListener(logicalAgent));
    }

    private void loadMessagePane() {
        ArrayList<AnchorPane> messages = new ArrayList<>();
        for (DBMessage message :
                group.getMessages()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(MESSAGE_CONFIG.getProperty(String.class, "MessageComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        new MessageLoader(logicalAgent, message, viewer, fxmlLoader.getController(), group);
                        messages.add(anchorPane);
                    } catch (IOException e) {
                        logger.error("Loading Message " + message.getId() + " Failed");
                    }
                }
            });
        }
        controller.build(messages);
    }

    public void updateGroup(ArrayList<DBMessage> messages) {
        if (hasNewMessage(messages)) {
            group.setMessages(messages);
            loadMessagePane();
        }
    }

    public boolean hasNewMessage(ArrayList<DBMessage> messages) {
        if (messages.size() == group.getMessages().size()) {
            for (int i = 0; i < group.getMessages().size(); i++) {
                if (!group.getMessages().get(i).getText().equals(messages.get(i).getText())) return true;
                if (!group.getMessages().get(i).getStatus().equals(messages.get(i).getStatus())) return true;
            }
        } else {
            return true;
        }
        return false;
    }

}
