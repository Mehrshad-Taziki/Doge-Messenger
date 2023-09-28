package graphics.pageLoader;

import enums.ChatContainerType;
import interfaces.ChatContainer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import listener.BackListener;
import listener.RequestListener;
import logics.LogicalAgent;
import model.DB.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.chatBox.ChatBoxController;
import pages.chatBox.chatComponents.contact.loader.ContactLoader;
import pages.chatBox.chatComponents.group.loader.GroupLoader;
import pages.chatBox.chatComponents.privateChat.loader.PrivateChatLoader;
import pages.chatBox.chatComponents.savedMessage.loader.SavedMessageLoader;
import tools.config.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ChatBoxLoader {
    private static final Logger logger = LogManager.getLogger(ChatBoxLoader.class);
    private static final Config chatBoxConfig = Config.getConfig("chatBox");
    private final LogicalAgent logicalAgent;
    private ChatBoxController controller;
    private DBUser viewer;
    private GroupLoader groupLogic;
    private PrivateChatLoader privateChatLoader;
    private SavedMessageLoader savedMessageLoader;


    public ChatBoxLoader(LogicalAgent graphicalAgent) {
        this.logicalAgent = graphicalAgent;
    }

    public void loadPage(DBUser viewer, ChatBoxController controller) {
        this.controller = controller;
        this.viewer = viewer;
        loadActionListener();
        loadCreateListener();
        loadCreateContactListener();
    }

    private void loadActionListener() {
        controller.setActionListener(new BackListener(logicalAgent));
        controller.setSearchUserListener(new RequestListener(logicalAgent));
    }

    public void updateChat(ChatContainerType type, ArrayList<DBMessage> messages) {
        if (controller != null) {
            switch (type) {
                case PRIVATECHAT -> privateChatLoader.updatePrivateChat(messages);
                case GROUP -> groupLogic.updateGroup(messages);
                case NONE -> removedChat();
                case SAVEDMESSAGE -> savedMessageLoader.updateSavedMessage(messages);
            }
        }
    }

    private void loadCreateListener() {
        controller.setCreateListener(new RequestListener(logicalAgent));
    }

    private void loadCreateContactListener() {
        controller.setCreateContactListener(new RequestListener(logicalAgent));
    }

    public void loadMessagePreviewList(HashSet<DBGroup> groups, HashSet<DBPrivateChat> privateChats, HashSet<DBContact> contacts) {
        if (controller != null) {
            System.out.println("solam da");
            controller.loadPrivateChatsPreview(this, privateChats);
            controller.loadGroupPreview(this, groups);
            controller.loadContactPreview(this, contacts);
        }
    }

    public void loadPrivateChatPane(ChatContainer chatContainer) {
        if (chatContainer instanceof DBPrivateChat) {
            DBPrivateChat privateChat = (DBPrivateChat) chatContainer;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(chatBoxConfig.getProperty("PrivateChatComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        privateChatLoader = new PrivateChatLoader(logicalAgent, privateChat, viewer, fxmlLoader.getController());
                        controller.loadChatPane(anchorPane);
                    } catch (IOException e) {
                        logger.error("Loading PrivateChat Scene Failed " + e.getMessage());
                    }
                }
            });
        }
        if (chatContainer instanceof DBSavedMessage) {
            DBSavedMessage savedMessage = (DBSavedMessage) chatContainer;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(chatBoxConfig.getProperty("SavedMessageComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        savedMessageLoader = new SavedMessageLoader(logicalAgent, savedMessage, viewer, fxmlLoader.getController());
                        controller.loadChatPane(anchorPane);
                    } catch (IOException e) {
                        logger.error("Loading SavedMessage Scene Failed " + e.getMessage());
                    }
                }
            });
        }

    }

    public void loadGroupChatPane(DBGroup group) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(chatBoxConfig.getProperty("GroupComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    groupLogic = new GroupLoader(logicalAgent, group, viewer, fxmlLoader.getController());
                    controller.loadChatPane(anchorPane);
                } catch (IOException e) {
                    logger.error("Loading Group Scene Failed " + e.getMessage());
                }
            }
        });

    }

    public void loadContactChatPane(DBContact contact) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(chatBoxConfig.getProperty("ContactComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    new ContactLoader(logicalAgent, contact, viewer, fxmlLoader.getController());
                    controller.loadChatPane(anchorPane);
                } catch (IOException e) {
                    logger.error("Loading Contact Scene Failed " + e.getMessage());
                }
            }
        });

    }

    public void removedChat() {
        controller.clearChatPane();
    }

    public DBUser getViewer() {
        return viewer;
    }

    public LogicalAgent getLogicalAgent() {
        return logicalAgent;
    }

}
