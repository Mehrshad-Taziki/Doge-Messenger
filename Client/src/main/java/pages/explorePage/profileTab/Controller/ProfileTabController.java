package pages.explorePage.profileTab.Controller;

import enums.UserAction;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import listener.Listener;
import listener.UserActionListener;
import model.DB.DBUser;
import responses.ShowProfileResponse;
import tools.ImageHandler;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class ProfileTabController implements Initializable {
    @FXML
    GridPane mainPane;
    @FXML
    GridPane postPane;
    @FXML
    GridPane infoPane;
    @FXML
    Label username;
    @FXML
    Label name;
    @FXML
    Label lastname;
    @FXML
    Label postCount;
    @FXML
    Label lastSeen;
    @FXML
    Button block;
    @FXML
    Button mute;
    @FXML
    Button follow;
    @FXML
    Button message;
    @FXML
    Button backButton;
    @FXML
    Label biography;
    @FXML
    Label number;
    @FXML
    Label email;
    @FXML
    Label followingCount;
    @FXML
    Label followerCount;
    @FXML
    Label birthDay;
    @FXML
    Label status;
    @FXML
    Circle profileImage;
    private Listener backListener;
    private UserActionListener actionListener;


    public void fixLastSeen(DBUser user, ShowProfileResponse response) {
        Platform.runLater(() -> {
            if (user.getLastSeenType() == 1) {
                lastSeen.setText(user.getLastSeen().getTime());
            }
            if (user.getLastSeenType() == 3) {
                lastSeen.setText("Recently");
            }
            if (user.getLastSeenType() == 2) {
                if (response.isIfFollowed()) {
                    lastSeen.setText(user.getLastSeen().getTime());
                } else {
                    lastSeen.setText("Recently");
                }
            }
        });

    }

    public void fixBlockButton(ShowProfileResponse response) {
        Platform.runLater(() -> {
            if (response.isIfBlocked1()) {
                follow.setVisible(false);
                message.setVisible(false);
                block.setText("Block");
            } else if (response.isIfBlocked2()) {
                follow.setVisible(false);
                message.setVisible(false);
                block.setText("UnBlock");
            } else {
                follow.setVisible(true);
                message.setVisible(true);
                block.setText("Block");
            }
        });

    }

    public void fixFollowButton(ShowProfileResponse response) {
        Platform.runLater(() -> {
            if (response.isIfFollowed()) {
                follow.setText("UnFollow");
            } else if (response.isIfRequested()) {
                follow.setText("Requested");
            } else {
                follow.setText("Follow");
            }
        });

    }

    public void fixMuteButton(ShowProfileResponse response) {
        Platform.runLater(() -> {
            if (response.isIfMuted()) {
                mute.setText("UnMute");
            } else {
                mute.setText("Mute");
            }
        });

    }

    public void build(DBUser user, ShowProfileResponse response) {
        Platform.runLater(() -> {
            mainPane.setVisible(true);
            postPane.getChildren().clear();
            username.setText(user.getUsername());
            name.setText(user.getName());
            lastname.setText(user.getLastName());
            postCount.setText(String.valueOf(user.getPosts().size()));
            if (!user.getImageInString().equals("") && user.getImageInString() != null) {
                byte[] imageInByte = Base64.getDecoder().decode(user.getImageInString());
                Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                profileImage.setFill(new ImagePattern(image));
                profileImage.setVisible(true);
            }
            fixLastSeen(user, response);
            fixBlockButton(response);
            fixFollowButton(response);
            fixMuteButton(response);
            if (response.hasAccess()) {
                postPane.setVisible(true);
            }
            backButton.setVisible(true);
        });

    }

    public void block() {
        actionListener.listen(getAction("block"), username.getText());
    }

    public void follow() {
        actionListener.listen(getAction("follow"), username.getText());
    }

    public void message() {
        actionListener.listen(getAction("message"), username.getText());
    }

    public void mute() {
        actionListener.listen(getAction("mute"), username.getText());
    }

    public UserAction getAction(String type) {
        switch (type) {
            case "block" -> {
                if (block.getText().equals("Block")) return UserAction.BLOCK;
                return UserAction.UNBLOCK;
            }
            case "follow" -> {
                if (follow.getText().equals("Follow")) return UserAction.FOLLOW;
                if (follow.getText().equals("Requested")) return UserAction.UNREQUEST;
                return UserAction.UNFOLLOW;
            }
            case "message" -> {
                return UserAction.MESSAGE;
            }
            case "mute" -> {
                if (mute.getText().equals("Mute")) return UserAction.MUTE;
                return UserAction.UNMUTE;
            }
        }
        return null;
    }

    public void loadInfo(DBUser user, boolean followed) {
        Platform.runLater(() -> {
            if (user.getBiography().equals("-")) {
                biography.setText("No Biography");
            } else {
                biography.setText(user.getBiography());
            }
            if (user.isPublicNumber()) {
                number.setText(user.getPhoneNumber());
            } else {
                number.setText("Hidden");
            }
            if (user.isPublicEmail()) {
                email.setText(user.getEmail());
            } else {
                email.setText("Hidden");
            }
            if (user.isPublicBirthday()) {
                if (!user.getBirthDay().getDate().equals("0-0-0")) {
                    birthDay.setText("No Birthday");
                } else {
                    birthDay.setText(user.getBirthDay().getDate());
                }
            } else {
                birthDay.setText("Hidden");
            }
//        followerCount.setText(String.valueOf(followersCount));
//        followingCount.setText(String.valueOf(followingsCount));
            if (followed) {
                status.setText("This User Is Following You");
            } else {
                status.setText("This User Is Not Following You");
            }
        });

    }


    public void loadPostPane(AnchorPane anchorPane) {
        Platform.runLater(() -> {
            postPane.getChildren().clear();
            postPane.add(anchorPane, 0, 0);
        });

    }

    public void back() {
        backListener.listen();
    }

    public void showButtons() {
        Platform.runLater(() -> {
            message.setVisible(true);
            follow.setVisible(true);
            block.setVisible(true);
            mute.setVisible(true);
        });

    }


    public void hideButtons() {
        Platform.runLater(() -> {
            message.setVisible(false);
            follow.setVisible(false);
            block.setVisible(false);
            mute.setVisible(false);
        });

    }

    public void setBackListener(Listener backListener) {
        this.backListener = backListener;
    }

    public void setActionListener(UserActionListener listener) {
        this.actionListener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            mainPane.setVisible(false);
            infoPane.setVisible(true);
            postPane.setVisible(false);
            backButton.setVisible(false);
        });

    }
}
