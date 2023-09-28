package pages.explorePage.profileTab.loader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import listener.BackListener;
import logics.LogicalAgent;
import media.post.logic.PostLogic;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.explorePage.profileTab.Controller.ProfileTabController;
import pages.explorePage.profileTab.listener.UserActionsListener;
import responses.ShowProfileResponse;
import tools.config.Config;

import java.io.IOException;


public class ProfileTabLoader {
    private static final Logger logger = LogManager.getLogger(ProfileTabLoader.class);
    private final LogicalAgent logicalAgent;
    private final ProfileTabController controller;
    private final DBUser viewer;
    private final DBUser user;
    Config POST_CONFIG = Config.getConfig("explorePage");

    public ProfileTabLoader(LogicalAgent logicalAgent, DBUser viewer, DBUser user, ProfileTabController controller, ShowProfileResponse response) {
        this.logicalAgent = logicalAgent;
        this.controller = controller;
        this.viewer = viewer;
        this.user = user;
        loadListeners();
        build(user, response);
    }

    public void loadListeners() {
        controller.setBackListener(new BackListener(logicalAgent));
        controller.setActionListener(new UserActionsListener(logicalAgent));
    }

    public void build(DBUser user, ShowProfileResponse response) {
        controller.showButtons();
        controller.build(user, response);
        controller.loadInfo(user, response.isIfFollowed());
        if (viewer.getUsername().equals(user.getUsername())) {
            controller.hideButtons();
        }
        if (response.hasAccess()) {
            loadPosts();
        }
    }

    private void loadPosts() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(POST_CONFIG.getProperty(String.class, "postComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    controller.loadPostPane(anchorPane);
                    new PostLogic(logicalAgent, viewer, fxmlLoader.getController(), user.getPosts());
                } catch (IOException e) {
                    logger.error("Loading Posts Of " + user.getUsername() + " Failed");
                }
            }
        });
    }
    public String getUsername(){
        return user.getUsername();
    }
}
