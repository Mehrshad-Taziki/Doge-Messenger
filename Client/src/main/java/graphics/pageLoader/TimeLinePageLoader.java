package graphics.pageLoader;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import logics.LogicalAgent;
import media.post.logic.PostLogic;
import model.DB.DBPost;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.timeLinePage.TimeLinePageController;
import tools.config.Config;

import java.io.IOException;
import java.util.ArrayList;

public class TimeLinePageLoader {
    private static final Logger logger = LogManager.getLogger(TimeLinePageLoader.class);
    private static final Config TIMELINE_CONFIG = Config.getConfig("timeLinePage");
    private final LogicalAgent logicalAgent;
    private ArrayList<DBPost> selectedPosts;
    private TimeLinePageController controller;
    private DBUser viewer;
    private PostLogic postLogic;


    public TimeLinePageLoader(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    public void loadPage(DBUser user, TimeLinePageController controller, ArrayList<DBPost> selectedPosts) {
        this.controller = controller;
        this.viewer = user;
        this.selectedPosts = selectedPosts;
        loadMainPane();
    }


    public void updatePost(DBPost post) {
        if (postLogic != null) {
            postLogic.updatePost(post);
        }
    }

    private void loadMainPane() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(TIMELINE_CONFIG.getProperty(String.class, "postComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    controller.loadPane(anchorPane);
                    postLogic = new PostLogic(logicalAgent, viewer, fxmlLoader.getController(), selectedPosts);
                } catch (IOException e) {
                    logger.error("Loading TimeLine Page Failed " + e.getMessage());
                }
            }
        });
    }
}
