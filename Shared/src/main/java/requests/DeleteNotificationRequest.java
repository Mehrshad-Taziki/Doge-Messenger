package requests;

import model.Notification;
import responses.Response;

public class DeleteNotificationRequest extends Request {
    private final Notification notification;

    public DeleteNotificationRequest(Notification notification) {
        this.notification = notification;
    }

    @Override
    public Response takeAct(RequestHandler requestHandler) {
        return requestHandler.deleteNotification(notification);
    }
}
