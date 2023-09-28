package graphics.pageLoader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import listComponents.userPreview.UserPreview;
import listComponents.userRequestPreview.UserRequest;
import logics.LogicalAgent;
import media.post.logic.PostLogic;
import model.DB.DBPost;
import model.DB.DBUser;
import model.Notification;
import notification.logic.NotificationLogic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.explorePage.profileTab.loader.ProfileTabLoader;
import pages.personalPage.PersonalPageController;
import pages.personalPage.editTab.EditTabLoader;
import responses.ShowProfileResponse;
import tools.config.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class PersonalPageLoader {
    private static final Logger logger = LogManager.getLogger(PersonalPageLoader.class);
    private static final Config PERSONAL_CONFIG = Config.getConfig("personalPage");
    private static final Config PREVIEW_CONFIG = Config.getConfig("explorePage");
    PersonalPageController controller;
    DBUser viewer;
    LogicalAgent logicalAgent;
    PostLogic postLogic;
    EditTabLoader editLogic;

    public PersonalPageLoader(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    public void updatePost(DBPost post) {
        if (postLogic != null) {
            postLogic.updatePost(post);
        }
    }

    public void updateLists(ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests) {
        if (controller != null) {
            loadLists(followers, followings, blacklist, requests);
        }
    }

    public void updatePosts(ArrayList<DBPost> posts) {
        if (postLogic != null) {
            postLogic.resetPostIndex(posts);
        }
    }

    public void showProfile(ShowProfileResponse response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PREVIEW_CONFIG.getProperty(String.class, "profileComponent")));
                    Parent root = fxmlLoader.load();
                    new ProfileTabLoader(logicalAgent, viewer, response.getUser(), fxmlLoader.getController(), response);
                    Scene scene = new Scene(root);
                    logicalAgent.setScene(scene);
                } catch (IOException e) {
                    logger.error("Loading Profile Of " + viewer.getUsername() + "/" + viewer.getId() + " Failed");
                }
            }
        });
    }

    public void rebuildEdit(DBUser user) {
        editLogic.rebuildController(user);
    }

    public void loadPage(DBUser user, PersonalPageController controller, ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests) {
        this.controller = controller;
        this.viewer = user;
        loadPostPane();
        loadEditPane();
        loadLists(followers, followings, blacklist, requests);
    }

    private void loadPostPane() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PERSONAL_CONFIG.getProperty(String.class, "postComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    controller.setPostPane(anchorPane);
                    postLogic = new PostLogic(logicalAgent, viewer, fxmlLoader.getController(), viewer.getPosts());
                } catch (IOException e) {
                    logger.error("Loading Post Component Failed " + e.getMessage());
                }
            }
        });
    }

    public void loadLists(ArrayList<DBUser> followers, ArrayList<DBUser> followings, ArrayList<DBUser> blacklist, ArrayList<DBUser> requests) {
        loadUserPreviewList(controller.getFollowersPane(), followers);
        loadUserPreviewList(controller.getFollowingsPane(), followings);
        loadUserPreviewList(controller.getBlackListPane(), blacklist);
        loadPendingTab(requests);
        loadNotificationTab();
    }

    private void loadEditPane() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(PERSONAL_CONFIG.getProperty(String.class, "editTab")));
                    StackPane stackPane = fxmlLoader.load();
                    controller.setEditPane(stackPane);
                    editLogic = new EditTabLoader(logicalAgent, fxmlLoader.getController(), viewer);
                } catch (IOException e) {
                    logger.error("Loading Edit Component Failed " + e.getMessage());
                }
            }
        });
    }

    private void loadPendingTab(ArrayList<DBUser> requests) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.getPendingPane().getChildren().clear();
                    for (DBUser user :
                            requests) {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(PERSONAL_CONFIG.getProperty(String.class, "userRequestComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        controller.addPending(anchorPane);
                        new UserRequest(logicalAgent, viewer, user, fxmlLoader.getController());
                    }
                } catch (IOException e) {
                    logger.error("Loading Pending List Failed " + e.getMessage());
                }
            }
        });
    }

    private void loadNotificationTab() {
        Platform.runLater(() -> controller.getNotificationPane().getChildren().clear());
        Iterator<Notification> notificationIterator = viewer.getNotification().descendingIterator();
        while (notificationIterator.hasNext()) {
            Notification notification = notificationIterator.next();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource(PERSONAL_CONFIG.getProperty(String.class, "notificationComponent")));
                        AnchorPane anchorPane = fxmlLoader.load();
                        new NotificationLogic(logicalAgent, fxmlLoader.getController(), notification);
                        controller.addNotification(anchorPane);
                    } catch (IOException e) {
                        logger.error("Loading Notification List Failed " + e.getMessage());
                    }
                }
            });
        }
    }


    private void loadUserPreviewList(GridPane pane, ArrayList<DBUser> users) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    pane.getChildren().clear();
                    for (DBUser user :
                            users) {
                        if (user.isActive()) {
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource(PERSONAL_CONFIG.getProperty(String.class, "userPreviewComponent")));
                            AnchorPane anchorPane = fxmlLoader.load();
                            new UserPreview(logicalAgent, user, viewer, fxmlLoader.getController());
                            pane.add(anchorPane, 0, pane.getRowCount() + 1);
                        }
                    }
                } catch (IOException e) {
                    logger.error("Loading UserPreview List Failed " + e.getMessage());
                }
            }
        });
    }
}
