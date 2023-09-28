package pages.chatBox.chatComponents.message.loader;


import interfaces.ChatContainer;
import logics.LogicalAgent;
import model.DB.DBMessage;
import model.DB.DBUser;
import pages.chatBox.chatComponents.message.controller.MessageController;
import pages.chatBox.chatComponents.message.listener.MessageActionListener;
import requests.DeleteMessageRequest;
import requests.EditMessageRequest;

public class MessageLoader {
    protected final LogicalAgent logicalAgent;
    private final DBMessage message;
    private final MessageController controller;
    private final ChatContainer chatContainer;
    private final boolean isSentByViewer;

    public MessageLoader(LogicalAgent logicalAgent, DBMessage message, DBUser viewer, MessageController controller, ChatContainer chatContainer) {
        this.logicalAgent = logicalAgent;
        this.message = message;
        this.controller = controller;
        this.isSentByViewer = viewer.getUsername().equals(message.getUser().getUsername());
        this.chatContainer = chatContainer;
        loadActionListeners();
        controller.build(message, isSentByViewer);
    }


    private void loadActionListeners() {
        controller.setMessageActionListener(new MessageActionListener(this));
        controller.setRequestListener(text -> logicalAgent.addRequest(new EditMessageRequest(message.getId(), text)));
    }

    public void deleteMessage() {
        logicalAgent.addRequest(new DeleteMessageRequest(message.getId()));
    }

    public void editMessage() {
        if (!message.isDeleted() && isSentByViewer) {
            controller.showEditPane();
        }
    }

}
