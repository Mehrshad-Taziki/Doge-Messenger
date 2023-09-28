package notification.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import listener.Listener;
import model.Notification;

public class NotificationController {
    private Listener deleteListener;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label messageLabel;

    public void setDeleteListener(Listener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void delete() {
        deleteListener.listen();
    }

    public void build(Notification notification) {
        usernameLabel.setText(notification.getUsername());
        messageLabel.setText(notification.getText());
    }

}
