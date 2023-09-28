package model;

import interfaces.ChatContainer;

import java.util.ArrayList;

public class SavedMessage implements ChatContainer {
    private final String username;
    private final ArrayList<Message> messages;
    private int unreadMessages;

    public SavedMessage(String username) {
        this.username = username;
        this.messages = new ArrayList<>();
        this.unreadMessages = 0;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int getUnreadMessages() {
        return unreadMessages;
    }

    @Override
    public void newMessage() {
        unreadMessages = 0;
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
}
