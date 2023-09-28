package listener;


import logics.LogicalAgent;

public class BackListener implements Listener {
    LogicalAgent logicalAgent;

    public BackListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen() {
        logicalAgent.goToPreviousPage();
    }
}
