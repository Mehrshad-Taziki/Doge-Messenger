package listComponents.groupPreview;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import listener.RequestListener;
import model.DB.DBGroup;
import requests.SearchGroupRequest;

public class GroupPreviewController {

    @FXML
    private Label unreadMessages;
    @FXML
    private Label groupNameLabel;

    private RequestListener listener;

    public void build(DBGroup group) {
        groupNameLabel.setText(group.getGroupName());
        unreadMessages.setText(String.valueOf(group.getUnreadMessages()));
    }

    public void show() {
        listener.listen(new SearchGroupRequest(groupNameLabel.getText()));
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }
}
