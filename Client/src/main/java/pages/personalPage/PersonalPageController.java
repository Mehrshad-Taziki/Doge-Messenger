package pages.personalPage;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import listener.Listener;
import listener.PostListener;
import requests.PostRequest;
import tools.ImageHandler;
import tools.config.Sizes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PersonalPageController implements Initializable {
    BufferedImage postBufferedImage;
    @FXML
    private GridPane editPane;
    @FXML
    private TextArea postTextField;
    @FXML
    private GridPane followersPane;
    @FXML
    private GridPane followingsPane;
    @FXML
    private GridPane blackListPane;
    @FXML
    private GridPane pendingPane;
    @FXML
    private GridPane notificationPane;
    @FXML
    private GridPane postPane;
    private PostListener postListener;
    private Listener backListener;

    public void setPostListener(PostListener postListener) {
        this.postListener = postListener;
    }

    public void setBackListener(Listener backListener) {
        this.backListener = backListener;
    }

    public void post() {
        if (postTextField.getText().equals("")) return;
        PostRequest request = new PostRequest(postTextField.getText(), ImageHandler.toByteArray(postBufferedImage, "png"));
        postListener.listen(request);
        Platform.runLater(() -> postTextField.setText(""));
        Platform.runLater(() -> postBufferedImage = null);
    }

    public void uploadImage() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().addAll(extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            postBufferedImage = ImageIO.read(file);
        } catch (Throwable ignored) {
        }
    }

    public void back() {
        backListener.listen();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postTextField.setWrapText(true);
        postTextField.setMaxWidth(Sizes.motherLabel());
    }

    public GridPane getFollowersPane() {
        return followersPane;
    }

    public GridPane getFollowingsPane() {
        return followingsPane;
    }

    public GridPane getBlackListPane() {
        return blackListPane;
    }

    public GridPane getNotificationPane() {
        return notificationPane;
    }

    public GridPane getPendingPane() {
        return pendingPane;
    }

    public GridPane getPostPane() {
        return postPane;
    }

    public void setPostPane(AnchorPane anchorPane) {
        Platform.runLater(() -> {
            postPane.getChildren().clear();
            postPane.add(anchorPane, 0, 0);
        });

    }

    public void setEditPane(StackPane stackPane) {
        Platform.runLater(() -> editPane.add(stackPane, 0, 0));
    }

    public void addPending(AnchorPane anchorPane) {
        Platform.runLater(() -> pendingPane.add(anchorPane, 0, pendingPane.getRowCount() + 1));
    }

    public void addNotification(AnchorPane anchorPane) {
        Platform.runLater(() -> notificationPane.add(anchorPane, 0, notificationPane.getRowCount() + 1));
    }
}
