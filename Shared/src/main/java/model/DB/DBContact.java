package model.DB;

import interfaces.ChatComponent;
import model.Contact;

import java.util.HashSet;

public class DBContact implements ChatComponent {
    private final String ContactName;
    private final DBUser viewerUser;
    private final HashSet<DBUser> members;


    public DBContact(Contact contact) {
        this.ContactName = contact.getContactName();
        this.viewerUser = new DBUser(contact.getViewerUser());
        this.members = new HashSet<>();
        contact.getMembers().forEach(user -> this.members.add(new DBUser(user)));
    }

    public DBContact(String groupName, DBUser viewerUser, HashSet<DBUser> users) {
        this.ContactName = groupName;
        this.viewerUser = viewerUser;
        this.members = users;
    }

    public DBUser getViewerUser() {
        return viewerUser;
    }

    public String getContactName() {
        return ContactName;
    }

    public HashSet<DBUser> getMembers() {
        return members;
    }
}
