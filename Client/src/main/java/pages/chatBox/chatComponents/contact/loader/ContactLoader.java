package pages.chatBox.chatComponents.contact.loader;

import listener.RequestListener;
import logics.LogicalAgent;
import model.DB.DBContact;
import model.DB.DBUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.chatBox.chatComponents.contact.controller.ContactController;

public class ContactLoader {
    private static final Logger logger = LogManager.getLogger(ContactLoader.class);
    private final LogicalAgent logicalAgent;
    private final DBContact contact;
    private final ContactController controller;
    private final DBUser viewer;

    public ContactLoader(LogicalAgent logicalAgent, DBContact contact, DBUser viewer, ContactController controller) {
        this.logicalAgent = logicalAgent;
        this.contact = contact;
        this.controller = controller;
        this.viewer = viewer;
        loadListener();
    }


    private void loadListener() {
        controller.setMessageListener(new RequestListener(logicalAgent));
        controller.setUserListener(new RequestListener(logicalAgent));
        logger.info("Contact Of User " + viewer.getUsername() + " : " + contact.getContactName()  + " Loaded");
    }

}
