package listComponents.groupPreview;

import graphics.pageLoader.ChatBoxLoader;
import listener.RequestListener;
import model.DB.DBGroup;

public class GroupPreview {
    private final ChatBoxLoader chatBox;
    private final DBGroup group;
    private final GroupPreviewController controller;

    public GroupPreview(ChatBoxLoader chatBox, DBGroup group, GroupPreviewController controller) {
        this.chatBox = chatBox;
        this.group = group;
        this.controller = controller;
        loadListener();
        build();
    }

    private void loadListener() {
        controller.setListener(new RequestListener(chatBox.getLogicalAgent()));
    }

    private void build() {
        controller.build(group);
    }
}
