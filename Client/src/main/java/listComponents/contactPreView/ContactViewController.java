package listComponents.contactPreView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import listener.RequestListener;
import model.DB.DBContact;
import requests.SearchContactRequest;

public class ContactViewController {

    @FXML
    private Label contactNameLabel;

    private RequestListener listener;

    public void build(DBContact contact) {
        contactNameLabel.setText(contact.getContactName());
    }

    public void show() {
        listener.listen(new SearchContactRequest(contactNameLabel.getText()));
    }

    public void setListener(RequestListener listener) {
        this.listener = listener;
    }
}
