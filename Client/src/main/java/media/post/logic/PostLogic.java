package media.post.logic;

import enums.ChatContainerType;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import logics.LogicalAgent;
import media.listener.PostMediaActionListener;
import media.post.controller.PostController;
import model.DB.DBPost;
import model.DB.DBUser;
import requests.CommentRequest;
import requests.ForwardRequest;
import tools.config.Alerts;
import tools.config.Config;

import java.util.ArrayList;

public class PostLogic {
    private static final Config POST_CONFIG = Config.getConfig("posts");
    private final LogicalAgent logicalAgent;
    private final PostController controller;
    private final DBUser viewer;
    private ArrayList<DBPost> posts;
    private int postIndex = 0;


    public PostLogic(LogicalAgent logicalAgent, DBUser viewer, PostController controller, ArrayList<DBPost> posts) {
        this.logicalAgent = logicalAgent;
        this.controller = controller;
        this.posts = posts;
        this.viewer = viewer;
        start();
        loadPostListener();
        loadForwardListener();
        loadPostActionListeners();
    }

    public void start() {
        if (posts.isEmpty()) {
            controller.noPost();
        } else {
            controller.show(currentPost(), currentPost().getLikes().contains(viewer.getUsername()));
            controller.removeNoPost();

        }
    }

    public void updatePost(DBPost post) {
        posts.set(postIndex, post);
        controller.show(currentPost(), currentPost().getLikes().contains(viewer.getUsername()));
    }

    public void resetPostIndex(ArrayList<DBPost> posts) {
        this.posts = posts;
        this.postIndex = 0;
        start();
    }

    private void loadForwardListener() {
        controller.setForwardListener(string -> logicalAgent.addRequest(new ForwardRequest(string, ChatContainerType.PRIVATECHAT, currentPost().getId())));
        controller.setGroupForwardListener(string -> logicalAgent.addRequest(new ForwardRequest(string, ChatContainerType.GROUP, currentPost().getId())));
    }

    private void loadPostActionListeners() {
        controller.setActionListener(new PostMediaActionListener(logicalAgent, this));
    }

    private void loadPostListener() {
        controller.setCommentListener(this::comment);
    }

    public void comment(String text) {
        logicalAgent.addRequest(new CommentRequest(currentPost().getId(), text));
    }

    public void nextPost() {
        if (postIndex >= posts.size() - 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.setContentText(Alerts.lastPost());
            alert.showAndWait();
        } else {
            postIndex++;
            controller.show(currentPost(), currentPost().getLikes().contains(viewer.getUsername()));
        }
    }

    public void previousPost() {
        if (postIndex <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.setContentText(Alerts.firstPost());
            alert.showAndWait();
        } else {
            postIndex--;
            controller.show(currentPost(), currentPost().getLikes().contains(viewer.getUsername()));
        }
    }

    public DBPost currentPost() {
        return posts.get(postIndex);
    }

}
