package listComponents.privateChatPreview;

import requests.SearchPrivateChatRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import listener.RequestListener;
import model.DB.DBPrivateChat;
import model.DB.DBUser;

public class PrivateChatPreviewController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label unreadMessages;
    @FXML
    private Label usernameLabel;

    private RequestListener listener;

    public void build(DBPrivateChat privateChat, DBUser user) {
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        lastNameLabel.setText(user.getLastName());
        unreadMessages.setText(String.valueOf(privateChat.getUnreadMessages()));

    }

    public void show() {
        listener.listen(new SearchPrivateChatRequest(usernameLabel.getText()));
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }
}
