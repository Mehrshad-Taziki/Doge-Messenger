package listComponents.userRequestPreview;

import logics.LogicalAgent;
import requests.AnswerRequest;
import listComponents.userPreview.UserPreview;
import model.DB.DBUser;

public class UserRequest extends UserPreview {

    public UserRequest(LogicalAgent logicalAgent, DBUser viewer, DBUser user, UserRequestController controller) {
        super(logicalAgent, user, viewer, controller);
        loadActionListeners();
    }

    private void loadActionListeners() {
        controller.setRequestListener(answer -> logicalAgent.addRequest(new AnswerRequest(user.getUsername(), answer)));
    }

}
