package listComponents.userRequestPreview;

import enums.RequestAnswer;
import listComponents.userPreview.UserPreviewController;

public class UserRequestController extends UserPreviewController {
    public void accept() {
        requestListener.listen(RequestAnswer.ACCEPT);
    }

    public void hDecline() {
        requestListener.listen(RequestAnswer.HDECLINE);
    }

    public void decline() {
        requestListener.listen(RequestAnswer.DECLINE);
    }

}
