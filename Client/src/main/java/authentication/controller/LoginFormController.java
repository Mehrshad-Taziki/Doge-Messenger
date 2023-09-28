package authentication.controller;

import authentication.listener.LoginFormListener;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import listener.Listener;
import requests.LoginRequest;

public class LoginFormController {
    private LoginFormListener formListener;
    private Listener actionListener;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField usernameTextField;

    public void setFormListener(LoginFormListener loginFormListener) {
        this.formListener = loginFormListener;
    }

    public void setActionListener(Listener actionListener) {
        this.actionListener = actionListener;
    }

    public void login() {
        if (passwordTextField.getText().equals("") || usernameTextField.getText().equals("")) return;
        LoginRequest request =
                new LoginRequest(usernameTextField.getText(), passwordTextField.getText());
        formListener.listen(request);
        resetFields();
    }

    public void resetFields() {
        passwordTextField.setText("");
        usernameTextField.setText("");
    }

    public void back() {
        actionListener.listen();
    }
}
