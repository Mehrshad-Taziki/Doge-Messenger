package authentication.listener;

import logics.LogicalAgent;
import listener.Listener;

public class AuthExitListener implements Listener {
    LogicalAgent logicalAgent;

    public AuthExitListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen() {
        logicalAgent.reset();
    }
}
