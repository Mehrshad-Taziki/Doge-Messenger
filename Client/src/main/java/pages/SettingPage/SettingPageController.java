package pages.SettingPage;

import enums.SettingAction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import listener.Listener;
import listener.RequestListener;
import model.DB.DBUser;
import pages.SettingPage.Listener.UserSettingActionListener;
import requests.UserEditRequest;

public class SettingPageController {

    @FXML
    ToggleGroup lastSeen;
    private Listener backListener;
    private UserSettingActionListener actionListener;
    private RequestListener listener;
    @FXML
    private ToggleButton emailToggle;
    @FXML
    private ToggleButton numberToggle;
    @FXML
    private ToggleButton birthdayToggle;
    @FXML
    private RadioButton isPublicLastSeen;
    @FXML
    private RadioButton isFollowersLastSeen;
    @FXML
    private RadioButton isHiddenLastSeen;
    @FXML
    private ToggleButton privacyToggle;
    @FXML
    private PasswordField passwordField;


    public void birthdayToggle() {
        Platform.runLater(() -> birthdayToggle.setText(reverse(birthdayToggle.getText())));
    }

    public void emailToggle() {
        Platform.runLater(() -> emailToggle.setText(reverse(emailToggle.getText())));
    }

    public void numberToggle() {
        Platform.runLater(() -> numberToggle.setText(reverse(numberToggle.getText())));
    }

    public void privacyToggle() {
        Platform.runLater(() -> privacyToggle.setText(reverse(privacyToggle.getText())));
    }

    public void deActive() {
        actionListener.listen(SettingAction.DEACTIVE);
    }

    public void delete() {
        actionListener.listen(SettingAction.DELETE);
    }

    public void logOut() {
        actionListener.listen(SettingAction.LOGOUT);
    }

    public void back() {
        backListener.listen();
    }

    public void submit() {
        int lastSeenType = 1;
        if (isFollowersLastSeen.isSelected()) {
            lastSeenType = 2;
        }
        if (isHiddenLastSeen.isSelected()) {
            lastSeenType = 3;
        }
        UserEditRequest request = new UserEditRequest(!numberToggle.isSelected(), !emailToggle.isSelected(),
                !birthdayToggle.isSelected(), !privacyToggle.isSelected(), lastSeenType, passwordField.getText());
        listener.listen(request);
        Platform.runLater(() -> passwordField.setText(""));
    }

    public void build(DBUser user) {
        Platform.runLater(() -> {
            if (!user.isPublicEmail()) {
                emailToggle();
                emailToggle.setSelected(true);
            }
            if (!user.isPublicNumber()) {
                numberToggle();
                numberToggle.setSelected(true);
            }
            if (!user.isPublicBirthday()) {
                birthdayToggle();
                birthdayToggle.setSelected(true);
            }
            if (!user.isPublic()) {
                privacyToggle();
                privacyToggle.setSelected(true);
            }
            if (user.getLastSeenType() == 1) {
                isPublicLastSeen.setSelected(true);
            }
            if (user.getLastSeenType() == 2) {
                isFollowersLastSeen.setSelected(true);
            }
            if (user.getLastSeenType() == 3) {
                isHiddenLastSeen.setSelected(true);
            }
        });

    }

    public void setBackListener(Listener backListener) {
        this.backListener = backListener;
    }

    public void setActionListener(UserSettingActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }

    public String reverse(String string) {
        if (string.equals("Public")) {
            return "Private";
        }
        if (string.equals("Private")) {
            return "Public";
        }
        return "";
    }
}
