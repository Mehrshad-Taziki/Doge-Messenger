package authentication.listener;

import logics.LogicalAgent;
import requests.RegisterRequest;

public class RegisterListener implements RegistrationFormListener {
    LogicalAgent logicalAgent;

    public RegisterListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(RegisterRequest request) {
        logicalAgent.addRequest(request);
    }

}
