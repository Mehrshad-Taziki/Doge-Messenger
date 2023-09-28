package model;

import enums.SeenStatus;
import interfaces.ChatContainer;

import java.util.ArrayList;

public class PrivateChat implements ChatContainer {
    private final User viewerUser;
    private final User secondUser;
    private final ArrayList<Message> messages;
    private int unreadMessages;

    public PrivateChat(User firstUser, User secondUser) {
        this.viewerUser = firstUser;
        this.secondUser = secondUser;
        this.messages = new ArrayList<>();
        this.unreadMessages = 0;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public User getViewerUser() {
        return viewerUser;
    }

    public User getSecondUser() {
        return secondUser;
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

    public void deleteMessage(int id) {
        for (Message message :
                messages) {
            if (message.getId() == id) message.delete();
        }
    }

    public void seenMessages(SeenStatus status) {
        for (Message message :
                messages) {
            if (message.getUser().getUsername().equals(viewerUser.getUsername())) {
                message.setStatus(status);
            }
        }
    }
}
