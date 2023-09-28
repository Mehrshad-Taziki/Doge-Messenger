package media.comment.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import media.MediaController;
import model.DB.DBPost;
import tools.ImageHandler;
import tools.config.Sizes;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class CommentController extends MediaController implements Initializable {
    @FXML
    private Pane commentPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label postTextLabel;
    @FXML
    private Label motherUsernameLabel;
    @FXML
    private Label motherPostTextLabel;
    @FXML
    private Circle profileImagePost;
    @FXML
    private Circle profileImageComment;
    @FXML
    private Rectangle postImage;

    public void build(DBPost motherPost) {
        motherUsernameLabel.setText(motherPost.getWriter());
        motherPostTextLabel.setText(motherPost.getText());
        if (!motherPost.getImageInString().equals("") && motherPost.getImageInString() != null) {
            byte[] imageInByte = Base64.getDecoder().decode(motherPost.getImageInString());
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
            postImage.setFill(new ImagePattern(image));
            postImage.setVisible(true);
        }
    }

    public void show(DBPost post, DBPost motherPost, boolean liked) {
        fixButton(liked);
        commentPane.setVisible(true);
        usernameLabel.setText(post.getWriter());
        postTextLabel.setText(post.getText());
        if (!post.getWriterImageInString().equals("") && post.getWriterImageInString() != null) {
            byte[] imageInByte = Base64.getDecoder().decode(post.getWriterImageInString());
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
            profileImageComment.setFill(new ImagePattern(image));
            profileImageComment.setVisible(true);
        }
        if (!motherPost.getWriterImageInString().equals("") && motherPost.getWriterImageInString() != null) {
            byte[] imageInByte = Base64.getDecoder().decode(motherPost.getWriterImageInString());
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
            profileImagePost.setFill(new ImagePattern(image));
            profileImagePost.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postImage.setVisible(false);
        postTextLabel.setWrapText(true);
        postTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        postTextLabel.setMaxWidth(Sizes.commentLabel());
        motherPostTextLabel.setWrapText(true);
        motherPostTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        motherPostTextLabel.setMaxWidth(Sizes.motherLabel());
    }
}
