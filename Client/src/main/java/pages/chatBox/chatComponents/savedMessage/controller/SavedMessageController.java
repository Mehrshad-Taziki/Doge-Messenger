package pages.chatBox.chatComponents.savedMessage.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import listener.FileListener;
import listener.RequestListener;
import requests.SaveRequest;
import tools.ImageHandler;
import tools.config.Sizes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SavedMessageController implements Initializable {
    private BufferedImage messageBufferedImage;

    @FXML
    private GridPane messagesPane;

    @FXML
    private TextArea messageTextArea;

    private RequestListener messageListener;


    public void send() {
        if (messageTextArea.getText().equals("")) return;
        messageListener.listen(new SaveRequest(messageTextArea.getText(), ImageHandler.toByteArray(messageBufferedImage, "png")));
        messageTextArea.setText("");
        Platform.runLater(() -> messageBufferedImage = null);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageTextArea.setVisible(true);
        messageTextArea.setWrapText(true);
        messageTextArea.setMaxWidth(Sizes.savedMessage());
    }
}
