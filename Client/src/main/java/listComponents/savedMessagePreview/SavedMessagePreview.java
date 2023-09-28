package listComponents.savedMessagePreview;

import graphics.pageLoader.ChatBoxLoader;
import listener.RequestListener;

public class SavedMessagePreview {
    private final ChatBoxLoader chatBox;
    private final SavedMessagePreviewController controller;

    public SavedMessagePreview(ChatBoxLoader chatBox, SavedMessagePreviewController controller) {
        this.chatBox = chatBox;
        this.controller = controller;
        loadListener();
        build();
    }

    private void loadListener() {
        controller.setListener(new RequestListener(chatBox.getLogicalAgent()));
    }

    private void build() {
        controller.build();
    }
}
