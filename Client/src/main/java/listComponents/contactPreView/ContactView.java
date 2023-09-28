package listComponents.contactPreView;

import graphics.pageLoader.ChatBoxLoader;
import listener.RequestListener;
import model.DB.DBContact;

public class ContactView {
    private final ChatBoxLoader chatBox;
    private final DBContact contact;
    private final ContactViewController controller;

    public ContactView(ChatBoxLoader chatBox, DBContact contact, ContactViewController controller) {
        this.chatBox = chatBox;
        this.contact = contact;
        this.controller = controller;
        loadListener();
        build();
    }

    private void loadListener() {
        controller.setListener(new RequestListener(chatBox.getLogicalAgent()));
    }

    private void build() {
        controller.build(contact);
    }
}
