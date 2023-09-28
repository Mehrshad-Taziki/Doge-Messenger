package authentication.listener;

import logics.LogicalAgent;
import requests.LoginRequest;

public class LoginListener implements LoginFormListener {
    LogicalAgent logicalAgent;


    public LoginListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(LoginRequest request) {
        logicalAgent.addRequest(request);
    }


}
