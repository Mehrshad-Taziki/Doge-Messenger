package pages.SettingPage.Listener;

import enums.SettingAction;
import listener.SettingActionListener;
import logics.LogicalAgent;
import requests.SettingActionRequest;

public class UserSettingActionListener implements SettingActionListener {
    LogicalAgent logicalAgent;

    public UserSettingActionListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(SettingAction action) {
        logicalAgent.addRequest(new SettingActionRequest(action));
    }
}
