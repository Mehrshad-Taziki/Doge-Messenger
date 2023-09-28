package authentication.listener;

import requests.RegisterRequest;

public interface RegistrationFormListener {
    void listen(RegisterRequest request);
}
