package model;

import interfaces.ChatContainer;

import java.util.ArrayList;
import java.util.HashSet;

public class Group implements ChatContainer {
    private final String groupName;
    private final User viewerUser;
    private final HashSet<User> users;
    private final ArrayList<Message> messages;
    private final int ID;
    private int unreadMessages;


    public Group(String groupName, User viewerUser,int ID, HashSet<User> users) {
        this.groupName = groupName;
        this.viewerUser = viewerUser;
        this.messages = new ArrayList<>();
        this.users = users;
        this.unreadMessages = 0;
        this.ID = ID;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public User getViewerUser() {
        return viewerUser;
    }

    public String getGroupName() {
        return groupName;
    }

    public HashSet<User> getUsers() {
        return users;
    }

    @Override
    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    @Override
    public void newMessage() {
        unreadMessages++;
    }

    public void editMessage(int id, String text) {
        for (Message message :
                messages) {
            if (message.getId() == id) message.setText(text);
        }
    }

    public int getID() {
        return ID;
    }

    public void deleteMessage(int id) {
        for (Message message :
                messages) {
            if (message.getId() == id) message.delete();
        }
    }
}
