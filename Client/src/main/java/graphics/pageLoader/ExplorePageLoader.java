package graphics.pageLoader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import logics.LogicalAgent;
import media.post.logic.PostLogic;
import model.DB.DBPost;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.explorePage.ExplorePageController;
import pages.explorePage.profileTab.loader.ProfileTabLoader;
import requests.SearchProfileRequest;
import responses.ShowProfileResponse;
import tools.Loop;
import tools.config.Config;
import tools.config.FPS;

import java.io.IOException;
import java.util.ArrayList;

public class ExplorePageLoader {
    private static final Logger logger = LogManager.getLogger(ExplorePageLoader.class);
    private static final Config EXPLORE_CONFIG = Config.getConfig("explorePage");
    private final LogicalAgent logicalAgent;
    private Loop updateProfileLoop;
    private ArrayList<DBPost> selectedPosts;
    private ExplorePageController controller;
    private DBUser viewer;
    private ProfileTabLoader profileLogic;
    private PostLogic postLogic;


    public ExplorePageLoader(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    public void updatePost(DBPost post) {
        if (postLogic != null) {
            postLogic.updatePost(post);
        }
    }

    public void loadPage(DBUser user, ExplorePageController controller, ArrayList<DBPost> selectedPosts) {
        System.out.println("thirdStep");
        this.controller = controller;
        this.viewer = user;
        this.selectedPosts = selectedPosts;
        loadMainPane();
    }

    private void loadMainPane() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(EXPLORE_CONFIG.getProperty(String.class, "postComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    controller.loadPostPane(anchorPane);
                    postLogic = new PostLogic(logicalAgent, viewer, fxmlLoader.getController(), selectedPosts);
                } catch (IOException e) {
                    logger.error("Loading Explore Page Failed " + e.getMessage());
                }
            }
        });
    }

    private void loadProfilePane(DBUser user, ShowProfileResponse response) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(EXPLORE_CONFIG.getProperty(String.class, "profileComponent")));
                    StackPane stackPane = fxmlLoader.load();
                    controller.loadProfilePane(stackPane);
                    profileLogic = new ProfileTabLoader(logicalAgent, viewer, user, fxmlLoader.getController(), response);
                } catch (IOException e) {
                    logger.error("Loading Profile Scene Failed " + e.getMessage());
                }
            }
        });
    }

    public void buildProfile(ShowProfileResponse response) {
        if (profileLogic != null) {
            if (profileLogic.getUsername().equals(response.getUser().getUsername())) {
                profileLogic.build(response.getUser(), response);
            } else {
                updateProfileLoop.stop();
                updateProfileLoop = new Loop(FPS.updateTPS(), () -> logicalAgent.addRequest(new SearchProfileRequest(response.getUser().getUsername())));
                updateProfileLoop.start();
                loadProfilePane(response.getUser(), response);
            }
        } else {
            updateProfileLoop = new Loop(FPS.updateTPS(), () -> logicalAgent.addRequest(new SearchProfileRequest(response.getUser().getUsername())));
            updateProfileLoop.start();
            loadProfilePane(response.getUser(), response);
        }
    }

    public void stopLoop() {
        if (updateProfileLoop != null) {
            updateProfileLoop.stop();
        }
    }

}
