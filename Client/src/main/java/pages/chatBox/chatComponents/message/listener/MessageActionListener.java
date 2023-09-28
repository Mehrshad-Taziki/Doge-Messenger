package pages.chatBox.chatComponents.message.listener;

import listener.StringListener;
import pages.chatBox.chatComponents.message.loader.MessageLoader;

public class MessageActionListener implements StringListener {
    MessageLoader logic;

    public MessageActionListener(MessageLoader logic) {
        this.logic = logic;
    }

    @Override
    public void listen(String string) {
        if (string.equals("delete")) {
            logic.deleteMessage();
        }
        if (string.equals("showEdit")) {
            logic.editMessage();
        }
    }
}
