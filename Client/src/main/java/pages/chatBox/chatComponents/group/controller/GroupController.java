package pages.chatBox.chatComponents.group.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import listener.RequestListener;
import requests.AddUserToGroupRequest;
import requests.LeaveGroupRequest;
import requests.MessageRequest;
import tools.ImageHandler;
import tools.config.Sizes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GroupController implements Initializable {
    private BufferedImage messageBufferedImage;

    @FXML
    private GridPane messagesPane;
    @FXML
    private TextField addUserTextField;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private TextField waitTimeTextField;
    private RequestListener leaveListener;
    private RequestListener messageListener;
    private RequestListener userListener;


    public void add() {
        if (addUserTextField.getText().equals("")) return;
        userListener.listen(new AddUserToGroupRequest(addUserTextField.getText()));
        Platform.runLater(() -> addUserTextField.setText(""));
    }

    public void leave() {
        leaveListener.listen(new LeaveGroupRequest());
    }

    public void send() {
        if (messageTextArea.getText().equals("")) return;
        messageListener.listen(new MessageRequest(messageTextArea.getText(), waitTimeTextField.getText(), ImageHandler.toByteArray(messageBufferedImage, "png")));
        Platform.runLater(() -> messageTextArea.setText(""));
        Platform.runLater(() -> messageBufferedImage = null);
        Platform.runLater(() -> waitTimeTextField.setText("0"));
    }

    public void upload() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().addAll(extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            messageBufferedImage = ImageIO.read(file);
        } catch (Throwable ignored) {
        }
    }


    public void build(ArrayList<AnchorPane> messages) {
        Platform.runLater(() -> {
            messagesPane.getChildren().clear();
            for (AnchorPane message :
                    messages) {
                messagesPane.add(message, 0, messagesPane.getRowCount() + 1);
            }
        });
    }

    public void setMessageListener(RequestListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setUserListener(RequestListener userListener) {
        this.userListener = userListener;
    }

    public void setLeaveListener(RequestListener leaveListener) {
        this.leaveListener = leaveListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageTextArea.setVisible(true);
        messageTextArea.setWrapText(true);
        messageTextArea.setMaxWidth(Sizes.groupMessageArea());
        waitTimeTextField.setText("0");
        waitTimeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                waitTimeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.equals("")) {
                waitTimeTextField.setText("0");
            }
        });
    }

}
