package sender;

import requests.Request;
import responses.Response;

public interface RequestSender {
    Response sendRequest(Request request);
}
