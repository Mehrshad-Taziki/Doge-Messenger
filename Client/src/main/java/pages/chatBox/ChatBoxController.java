package pages.chatBox;


import graphics.pageLoader.ChatBoxLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import listComponents.contactPreView.ContactView;
import listComponents.groupPreview.GroupPreview;
import listComponents.privateChatPreview.PrivateChatPreview;
import listComponents.savedMessagePreview.SavedMessagePreview;
import listener.Listener;
import listener.RequestListener;
import model.DB.DBContact;
import model.DB.DBGroup;
import model.DB.DBPrivateChat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.SearchContactRequest;
import requests.SearchGroupRequest;
import requests.SearchPrivateChatRequest;
import tools.config.Config;

import java.io.IOException;
import java.util.HashSet;

public class ChatBoxController {
    private static final Logger logger = LogManager.getLogger(ChatBoxController.class);

    private static final Config chatBoxConfig = Config.getConfig("chatBox");

    @FXML
    private TextField messageTextField;
    @FXML
    private GridPane messagePreviewList;
    @FXML
    private GridPane chatContainerPane;
    @FXML
    private GridPane groupPreviewList;
    @FXML
    private TextField groupTextField;
    @FXML
    private GridPane contactPreviewList;
    @FXML
    private TextField contactTextField;

    private RequestListener searchPrivateChatListener;

    private RequestListener createGroupListener;

    private RequestListener createContactListener;

    private Listener actionListener;

    public void search() {
        if (messageTextField.getText().equals("")) return;
        searchPrivateChatListener.listen(new SearchPrivateChatRequest(messageTextField.getText()));
        Platform.runLater(() -> messageTextField.setText(""));
    }

    public void createGroup() {
        if (groupTextField.getText().equals("")) return;
        createGroupListener.listen(new SearchGroupRequest(groupTextField.getText()));
        Platform.runLater(() -> groupTextField.setText(""));
    }

    public void createContact() {
        if (contactTextField.getText().equals("")) return;
        createContactListener.listen(new SearchContactRequest(contactTextField.getText()));
        Platform.runLater(() -> contactTextField.setText(""));

    }

    public void setSearchUserListener(RequestListener searchUserListener) {
        this.searchPrivateChatListener = searchUserListener;
    }

    public void setCreateListener(RequestListener createListener) {
        this.createGroupListener = createListener;
    }

    public void setCreateContactListener(RequestListener createContactListener) {
        this.createContactListener = createContactListener;
    }

    public void setActionListener(Listener actionListener) {
        this.actionListener = actionListener;
    }

    public void back() {
        actionListener.listen();
    }

    public void loadPrivateChatsPreview(ChatBoxLoader chatBox, HashSet<DBPrivateChat> privateChats) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messagePreviewList.getChildren().clear();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(chatBoxConfig.getProperty("SavedMessagePreviewComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    new SavedMessagePreview(chatBox, fxmlLoader.getController());
                    messagePreviewList.add(anchorPane, 0, messagePreviewList.getRowCount() + 1);
                } catch (IOException e) {
                    logger.error("Loading SavedMessage Preview Failed");
                }
            }
        });
        for (DBPrivateChat privateChat :
                privateChats) {
            if (privateChat.getUnreadMessages() != 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource(chatBoxConfig.getProperty("UserPrivateChatPreviewComponent")));
                            AnchorPane pane = loader.load();
                            new PrivateChatPreview(chatBox, privateChat, loader.getController());
                            messagePreviewList.add(pane, 0, messagePreviewList.getRowCount() + 1);
                        } catch (IOException e) {
                            logger.error("Loading PrivateChat Preview Of" + privateChat.getViewerUser().getUsername() +
                                    "/" + privateChat.getSecondUser().getUsername() + " Failed");
                        }
                    }
                });
            }
        }
        for (DBPrivateChat privateChat :
                privateChats) {
            if (privateChat.getUnreadMessages() == 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource(chatBoxConfig.getProperty("UserPrivateChatPreviewComponent")));
                            AnchorPane pane = loader.load();
                            new PrivateChatPreview(chatBox, privateChat, loader.getController());
                            messagePreviewList.add(pane, 0, messagePreviewList.getRowCount() + 1);
                        } catch (IOException e) {
                            logger.error("Loading PrivateChat Preview Of" + privateChat.getViewerUser().getUsername() +
                                    "/" + privateChat.getSecondUser().getUsername() + " Failed");
                        }
                    }
                });
            }
        }
    }

    public void loadGroupPreview(ChatBoxLoader chatBox, HashSet<DBGroup> groups) {
        Platform.runLater(() -> groupPreviewList.getChildren().clear());
        for (DBGroup group :
                groups) {
            if (group.getUnreadMessages() != 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource(chatBoxConfig.getProperty("GroupPreviewComponent")));
                            AnchorPane pane = loader.load();
                            new GroupPreview(chatBox, group, loader.getController());
                            groupPreviewList.add(pane, 0, groupPreviewList.getRowCount() + 1);
                        } catch (IOException e) {
                            logger.error("Loading Group Preview Of " + group.getGroupName() + " Failed");
                        }
                    }
                });
            }
        }
        for (DBGroup group :
                groups) {
            if (group.getUnreadMessages() == 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource(chatBoxConfig.getProperty("GroupPreviewComponent")));
                            AnchorPane pane = loader.load();
                            new GroupPreview(chatBox, group, loader.getController());
                            groupPreviewList.add(pane, 0, groupPreviewList.getRowCount() + 1);
                        } catch (IOException e) {
                            logger.error("Loading Group Preview Of " + group.getGroupName() + " Failed");
                        }
                    }
                });
            }
        }
    }

    public void loadContactPreview(ChatBoxLoader chatBox, HashSet<DBContact> contacts) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    contactPreviewList.getChildren().clear();
                    DBContact allUsersContact = new DBContact("MessageAll", chatBox.getViewer(), new HashSet<>());
                    FXMLLoader allLoader = new FXMLLoader();
                    allLoader.setLocation(getClass().getResource(chatBoxConfig.getProperty("ContactViewComponent")));
                    AnchorPane allPane = allLoader.load();
                    new ContactView(chatBox, allUsersContact, allLoader.getController());
                    contactPreviewList.add(allPane, 0, contactPreviewList.getRowCount() + 1);
                } catch (IOException e) {
                    logger.error("Loading Contact Preview Of AllUsers Failed");
                }
            }
        });
        for (DBContact contact :
                contacts) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource(chatBoxConfig.getProperty("ContactViewComponent")));
                        AnchorPane pane = loader.load();
                        new ContactView(chatBox, contact, loader.getController());
                        contactPreviewList.add(pane, 0, contactPreviewList.getRowCount() + 1);
                    } catch (IOException e) {
                        logger.error("Loading Contact Preview Of " + contact.getContactName() + " Failed");
                    }
                }
            });
        }
    }

    public void loadChatPane(AnchorPane anchorPane) {
        Platform.runLater(() -> {
            chatContainerPane.getChildren().clear();
            chatContainerPane.add(anchorPane, 0, 0);
        });

    }

    public void clearChatPane() {
        Platform.runLater(() -> chatContainerPane.getChildren().clear());
    }
}
