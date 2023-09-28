package network;

import requests.Request;
import responses.Response;

public interface ResponseSender {
    Request getRequest();

    void sendResponse(Response response);

    void close();
}
