package model.DB;

import interfaces.ChatComponent;
import interfaces.ChatContainer;
import model.Message;
import model.SavedMessage;

import java.util.ArrayList;

public class DBSavedMessage implements ChatContainer , ChatComponent {
    private String username;
    private ArrayList<DBMessage> messages;
    private int unreadMessages;

    public DBSavedMessage(SavedMessage savedMessage) {
        this.username = savedMessage.getUsername();
        this.messages = new ArrayList<>();
        savedMessage.getMessages().forEach(message -> this.messages.add(new DBMessage(message)));
        this.unreadMessages = savedMessage.getUnreadMessages();
    }

    public ArrayList<DBMessage> getMessages() {
        return messages;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int getUnreadMessages() {
        return 0;
    }

    @Override
    public void newMessage() {

    }

    public void setMessages(ArrayList<DBMessage> messages) {
        this.messages = messages;
    }
}
