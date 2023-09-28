package model;


import java.util.HashSet;

public class Contact {
    private final String ContactName;
    private final User viewerUser;
    private final HashSet<User> members;


    public Contact(String groupName, User viewerUser, HashSet<User> users) {
        this.ContactName = groupName;
        this.viewerUser = viewerUser;
        this.members = users;
    }

    public User getViewerUser() {
        return viewerUser;
    }

    public String getContactName() {
        return ContactName;
    }

    public HashSet<User> getMembers() {
        return members;
    }

}
