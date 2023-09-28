package authentication.controller;

import authentication.listener.RegistrationFormListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import listener.Listener;
import requests.RegisterRequest;

public class RegisterFormController {
    private RegistrationFormListener formListener;
    private Listener actionListener;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField phoneNumberTextField;

    public void setFormListener(RegistrationFormListener registrationFormListener) {
        this.formListener = registrationFormListener;
    }

    public void setActionListener(Listener actionListener) {
        this.actionListener = actionListener;
    }

    public void register() {
        if (passwordTextField.getText().equals("") || usernameTextField.getText().equals("")) return;
        if (emailTextField.getText().equals("") || nameTextField.getText().equals("")) return;
        if (lastnameTextField.getText().equals("") || phoneNumberTextField.getText().equals("")) return;
        RegisterRequest request =
                new RegisterRequest(
                        usernameTextField.getText(),
                        passwordTextField.getText(),
                        emailTextField.getText(),
                        nameTextField.getText(),
                        lastnameTextField.getText(),
                        phoneNumberTextField.getText());
        formListener.listen(request);
    }

    public void back() {
        actionListener.listen();
    }
}
