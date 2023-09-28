package model.DB;

import interfaces.ChatComponent;
import interfaces.ChatContainer;
import model.Group;

import java.util.ArrayList;
import java.util.HashSet;

public class DBGroup implements ChatContainer, ChatComponent {
    private final String groupName;
    private final DBUser viewerUser;
    private final HashSet<DBUser> users;
    private final int unreadMessages;
    private final int ID;
    private ArrayList<DBMessage> messages;

    public DBGroup(Group group) {
        this.groupName = group.getGroupName();
        this.viewerUser = new DBUser(group.getViewerUser());
        this.messages = new ArrayList<>();
        group.getMessages().forEach(message -> this.messages.add(new DBMessage(message)));
        this.users = new HashSet<>();
        group.getUsers().forEach(user -> this.users.add(new DBUser(user)));
        this.unreadMessages = group.getUnreadMessages();
        this.ID = group.getID();
    }

    public ArrayList<DBMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<DBMessage> messages) {
        this.messages = messages;
    }

    public DBUser getViewerUser() {
        return viewerUser;
    }

    public String getGroupName() {
        return groupName;
    }

    public HashSet<DBUser> getUsers() {
        return users;
    }

    public int getID() {
        return ID;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    @Override
    public void newMessage() {

    }


}
