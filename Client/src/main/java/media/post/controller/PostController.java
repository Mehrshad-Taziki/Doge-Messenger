package media.post.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import media.MediaController;
import model.DB.DBPost;
import tools.ImageHandler;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class PostController extends MediaController implements Initializable {
    @FXML
    protected Label usernameLabel;
    @FXML
    protected Label postTextLabel;
    @FXML
    protected Label errorLabel;
    @FXML
    protected Pane postPane;
    @FXML
    protected Circle profileImage;
    @FXML
    protected ImageView postImage;
    @FXML
    protected Rectangle postFullImage;
    @FXML
    protected AnchorPane imagePane;
    @FXML
    protected Label datetimeLabel;

    public void show(DBPost post, boolean liked) {
        Platform.runLater(() -> {
            fixButton(liked);
            if (!post.getImageInString().equals("") && post.getImageInString() != null) {
                byte[] imageInByte = Base64.getDecoder().decode(post.getImageInString());
                Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                postImage.setImage(image);
                postFullImage.setFill(new ImagePattern(image));
                postImage.setVisible(true);
                postPane.setVisible(true);
            } else {
                postImage.setVisible(false);
            }
            postPane.setVisible(true);
            errorLabel.setVisible(false);
            usernameLabel.setText(post.getWriter());
            postTextLabel.setText(post.getText());
            datetimeLabel.setText(post.getDateTime().getDate());
            if (!post.getWriterImageInString().equals("") && post.getWriterImageInString() != null) {
                byte[] imageInByte = Base64.getDecoder().decode(post.getWriterImageInString());
                Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                profileImage.setFill(new ImagePattern(image));
                profileImage.setVisible(true);
            }
        });
    }

    public void close() {
        imagePane.setVisible(false);
    }

    public void showImage() {
        imagePane.setVisible(true);
    }

    public void noPost() {
        postPane.setVisible(false);
        errorLabel.setVisible(true);
    }

    public void removeNoPost() {
        errorLabel.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
        imagePane.setVisible(false);
        postTextLabel.setWrapText(true);
        postTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        postTextLabel.setMaxWidth(450);
        forwardMessageBox.setVisible(false);
    }
}
