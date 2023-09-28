package listener;


import logics.LogicalAgent;
import requests.Request;

public class RequestListener implements PostListener {
    LogicalAgent logicalAgent;

    public RequestListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(Request request) {
        logicalAgent.addRequest(request);
    }
}
