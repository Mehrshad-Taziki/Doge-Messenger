package network.handler;

import config.Alerts;
import database.Validation;
import enums.AlertType;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.ShowAlert;

public class AuthHandler {
    public boolean isValid(LoginRequest request) {
        if (Validation.isAvailableUser(request.getUserName())) {
            return false;
        }
        return Validation.correctUser(request.getUserName(), request.getPassword());
    }


    public boolean isValid(RegisterRequest request) {
        if (!Validation.isAvailableUser(request.getUserName())) {
            return false;
        }
        if (!Validation.isAvailableEMail(request.getEmail())) {
            return false;
        }
        if (!Validation.isValidEmail(request.getEmail())) {
            return false;
        }
        if (!Validation.isAvailableNumber(request.getPhoneNumber())) {
            return false;
        }
        return Validation.isValidPass(request.getPassword());
    }

    public ShowAlert getRegisterAlert(RegisterRequest request) {
        if (!Validation.isAvailableUser(request.getUserName())) {
            return new ShowAlert(Alerts.username(), AlertType.Error);
        }
        if (!Validation.isAvailableEMail(request.getEmail())) {
            return new ShowAlert(Alerts.emailA(), AlertType.Error);
        }
        if (!Validation.isValidEmail(request.getEmail())) {
            return new ShowAlert(Alerts.emailV(), AlertType.Error);
        }
        if (!Validation.isAvailableNumber(request.getPhoneNumber())) {
            return new ShowAlert(Alerts.numberAvailable(), AlertType.Error);
        }
        return new ShowAlert(Alerts.passwordNS(), AlertType.Error);
    }
}
