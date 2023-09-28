package pages.explorePage.profileTab.listener;

import enums.UserAction;
import logics.LogicalAgent;
import requests.UserActionRequest;
import listener.UserActionListener;

public class UserActionsListener implements UserActionListener {
    LogicalAgent logicalAgent;

    public UserActionsListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(UserAction action, String username) {
        logicalAgent.addRequest(new UserActionRequest(username, action));
    }
}
