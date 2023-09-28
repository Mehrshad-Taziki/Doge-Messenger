package media.comment.logic;

import logics.LogicalAgent;
import media.listener.CommentMediaListener;
import requests.CommentRequest;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import media.comment.controller.CommentController;
import model.DB.DBPost;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import listener.BackListener;
import tools.config.Alerts;

import java.util.ArrayList;

;

public class CommentLogic {
    private static final Logger logger = LogManager.getLogger(CommentLogic.class);
    private final LogicalAgent logicalAgent;
    private final CommentController controller;
    private final ArrayList<DBPost> comments;
    private final DBPost motherPost;
    private final DBUser viewer;
    private int commentIndex = 0;


    public CommentLogic(LogicalAgent logicalAgent,DBUser viewer, CommentController controller, ArrayList<DBPost> comments, DBPost motherPost) {
        this.logicalAgent = logicalAgent;
        this.controller = controller;
        this.comments = comments;
        this.viewer = viewer;
        this.motherPost = motherPost;
        controller.build(motherPost);
        start();
        loadBackListener();
        loadPostListener();
        loadPostActionListeners();
    }

    private void start() {
        controller.show(currentComment(), motherPost, isLikedBy());
    }

    private void loadBackListener() {
        controller.setBackListener(new BackListener(logicalAgent));
    }
    private void loadPostActionListeners() {
        controller.setActionListener(new CommentMediaListener(this, logicalAgent));
    }

    private void loadPostListener() {
        controller.setCommentListener(this::comment);
    }

    private void comment(String text){
        logicalAgent.addRequest(new CommentRequest(currentComment().getId(), text));
    }

    public void nextComment() {
        if (commentIndex >= comments.size() - 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Post Error");
            alert.setHeaderText(null);
            alert.setContentText(Alerts.lastComment());
            alert.showAndWait();
            String username = viewer.getUsername();
            logger.info(username + "/" + viewer.getId() + " Used Next On The Last Comment");
        } else {
            commentIndex++;
            controller.show(currentComment(), motherPost, isLikedBy());
        }
    }

    public void previousComment() {
        if (commentIndex <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Post Error");
            alert.setHeaderText(null);
            alert.setContentText(Alerts.firstComment());
            alert.showAndWait();
            String username = viewer.getUsername();
            logger.info(username + "/" + viewer.getId() + " Used Previous On The First Comment");
        } else {
            commentIndex--;
            controller.show(currentComment(), motherPost, isLikedBy());
        }
    }

    private boolean isLikedBy() {
        for (String user :
                currentComment().getLikes()) {
            if (user.equals(viewer.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public DBPost currentComment() {
        return comments.get(commentIndex);
    }


}
