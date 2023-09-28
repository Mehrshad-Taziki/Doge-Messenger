package notification.logic;

import logics.LogicalAgent;
import model.Notification;
import notification.controller.NotificationController;
import notification.listener.DeleteListener;
import requests.DeleteNotificationRequest;

public class NotificationLogic {
    private final LogicalAgent logicalAgent;
    private final NotificationController controller;
    private final Notification notification;

    public NotificationLogic(LogicalAgent logicalAgent, NotificationController controller, Notification notification) {
        this.logicalAgent = logicalAgent;
        this.controller = controller;
        this.notification = notification;
        controller.build(notification);
        loadNotificationActionListener();
    }

    private void loadNotificationActionListener() {
        controller.setDeleteListener(new DeleteListener(this));
    }

    public void deleteNotification() {
        logicalAgent.addRequest(new DeleteNotificationRequest(notification));
    }
}
