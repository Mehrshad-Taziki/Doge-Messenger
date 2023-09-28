package media;

import enums.PostActions;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import listener.Listener;
import listener.PostActionListener;
import listener.StringListener;

public class MediaController {
    protected PostActionListener actionListener;
    protected StringListener forwardListener;
    protected StringListener groupForwardListener;
    protected StringListener commentListener;
    protected Listener backListener;
    @FXML
    protected Button likeButton;
    @FXML
    protected HBox forwardMessageBox;
    @FXML
    protected TextField forwardTextField;
    @FXML
    protected TextField groupForwardTextField;
    @FXML
    protected TextField commentTextField;


    public void report() {
        actionListener.listen(PostActions.REPORT);
    }

    public void save() {
        actionListener.listen(PostActions.SAVE);
    }


    public void fixButton(boolean liked) {
        if (liked) {
            likeButton.setText("UnLike");
        } else {
            likeButton.setText("Like");
        }
    }


    public void comment() {
        if (commentTextField.getText().equals("")) return;
        commentListener.listen(commentTextField.getText());
        commentTextField.setText("");
    }

    public void next() {
        actionListener.listen(PostActions.NEXT);
    }

    public void previous() {
        actionListener.listen(PostActions.PREVIOUS);
    }

    public void like() {
        actionListener.listen(value(likeButton.getText()));
    }

    public void seeComments() {
        actionListener.listen(PostActions.SEECOMMENTS);
    }

    public void forward() {
        forwardMessageBox.setVisible(true);
    }

    public void forwardFor() {
        if (forwardTextField.getText().equals("")) return;
        forwardListener.listen(forwardTextField.getText());
        forwardMessageBox.setVisible(false);
    }

    public void repost() {
        actionListener.listen(PostActions.REPOST);
    }


    public void forwardToGroup() {
        if (groupForwardTextField.getText().equals("")) return;
        groupForwardListener.listen(groupForwardTextField.getText());
        forwardMessageBox.setVisible(false);
    }

    public void back() {
        backListener.listen();
    }

    public void setForwardListener(StringListener forwardListener) {
        this.forwardListener = forwardListener;
    }

    public void setGroupForwardListener(StringListener groupForwardListener) {
        this.groupForwardListener = groupForwardListener;
    }

    public void setActionListener(PostActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public PostActions value(String value) {
        if (value.equals("Like")) {
            return PostActions.LIKE;
        }
        return PostActions.UNLIKE;
    }

    public void setBackListener(Listener backListener) {
        this.backListener = backListener;
    }

    public void setCommentListener(StringListener commentListener) {
        this.commentListener = commentListener;
    }
}
