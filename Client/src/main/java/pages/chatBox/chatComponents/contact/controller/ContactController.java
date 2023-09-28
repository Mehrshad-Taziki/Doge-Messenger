package pages.chatBox.chatComponents.contact.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import listener.RequestListener;
import requests.AddUserToContactRequest;
import requests.MessageRequest;
import tools.ImageHandler;
import tools.config.Sizes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    private BufferedImage messageBufferedImage;
    @FXML
    private GridPane messagesPane;
    @FXML
    private TextField addUserTextField;
    @FXML
    private TextArea messageTextArea;
    private RequestListener messageListener;
    private RequestListener userListener;

    public void add() {
        if (addUserTextField.getText().equals("")) return;
        userListener.listen(new AddUserToContactRequest(addUserTextField.getText()));
        Platform.runLater(() -> addUserTextField.setText(""));
    }


    public void send() {
        if (messageTextArea.getText().equals("")) return;
        messageListener.listen(new MessageRequest(messageTextArea.getText(), "0", ImageHandler.toByteArray(messageBufferedImage, "png")));
        Platform.runLater(() -> messageTextArea.setText(""));
        Platform.runLater(() -> messageBufferedImage = null);
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


    public void setMessageListener(RequestListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setUserListener(RequestListener userListener) {
        this.userListener = userListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageTextArea.setVisible(true);
        messageTextArea.setWrapText(true);
        messageTextArea.setMaxWidth(Sizes.contactMessageArea());
    }

}
