package model.DB;

import interfaces.ChatComponent;
import interfaces.ChatContainer;
import model.PrivateChat;

import java.util.ArrayList;

public class DBPrivateChat implements ChatContainer , ChatComponent {
    private final DBUser viewerUser;
    private final DBUser secondUser;
    private ArrayList<DBMessage> messages;
    private final int unreadMessages;

    public DBPrivateChat(PrivateChat privateChat) {
        this.viewerUser = new DBUser(privateChat.getViewerUser());
        this.secondUser = new DBUser(privateChat.getSecondUser());
        this.messages = new ArrayList<>();
        privateChat.getMessages().forEach(message -> this.messages.add(new DBMessage(message)));
        this.unreadMessages = privateChat.getUnreadMessages();
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

    public DBUser getSecondUser() {
        return secondUser;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    @Override
    public void newMessage() {

    }

}
