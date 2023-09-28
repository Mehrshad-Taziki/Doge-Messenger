package notification.listener;

import listener.Listener;
import notification.logic.NotificationLogic;

public class DeleteListener implements Listener {
    NotificationLogic logic;

    public DeleteListener(NotificationLogic logic) {
        this.logic = logic;
    }

    @Override
    public void listen() {
        logic.deleteNotification();
    }

}
