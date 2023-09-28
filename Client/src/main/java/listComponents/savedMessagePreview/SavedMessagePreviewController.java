package listComponents.savedMessagePreview;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import listener.RequestListener;
import requests.GetSavedMessagesRequest;

public class SavedMessagePreviewController {

    @FXML
    private Label unreadMessages;


    private RequestListener listener;

    public void build() {
        unreadMessages.setText("0");
    }

    public void show() {
        listener.listen(new GetSavedMessagesRequest());
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }
}
