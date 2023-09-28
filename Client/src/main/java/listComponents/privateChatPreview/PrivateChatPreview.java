package listComponents.privateChatPreview;

import graphics.pageLoader.ChatBoxLoader;
import logics.LogicalAgent;
import listener.RequestListener;
import model.DB.DBPrivateChat;
import model.DB.DBUser;

import java.util.Objects;

public class PrivateChatPreview {
    private LogicalAgent logicalAgent;
    private DBPrivateChat privateChat;
    private PrivateChatPreviewController controller;
    private DBUser viewer;

    public PrivateChatPreview(ChatBoxLoader chatBox, DBPrivateChat privateChat, PrivateChatPreviewController controller) {
        this.privateChat = privateChat;
        this.logicalAgent = chatBox.getLogicalAgent();
        this.controller = controller;
        viewer = chatBox.getViewer();
        loadListener();
        build();
    }

    private void loadListener() {
        controller.setListener(new RequestListener(logicalAgent));
    }


    private void build() {
        if (Objects.nonNull(privateChat.getViewerUser()) && Objects.nonNull(privateChat.getSecondUser())) {
            if (viewer.equals(privateChat.getViewerUser())) {
                controller.build(privateChat, privateChat.getSecondUser());
            } else {
                controller.build(privateChat, privateChat.getViewerUser());
            }
        }
    }
}
