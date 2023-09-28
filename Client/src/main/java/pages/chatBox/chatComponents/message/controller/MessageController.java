package pages.chatBox.chatComponents.message.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import listener.StringListener;
import model.DB.DBMessage;
import model.DB.DBUser;
import tools.ImageHandler;
import tools.config.Sizes;

import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class MessageController implements Initializable {

    @FXML
    private Label senderTextLabel;

    @FXML
    private Label receiverTextLabel;

    @FXML
    private Label senderUsernameLabel;

    @FXML
    private Label receiverUserNameLabel;

    @FXML
    private VBox editMessagePane;

    @FXML
    private TextArea editTextArea;

    @FXML
    private Circle senderProfileImage;

    @FXML
    private Circle receiverProfileImage;

    @FXML
    private Rectangle SenderMessageImage;

    @FXML
    private Rectangle receiverMessageImage;

    @FXML
    private Label seenStatusLabel;

    private StringListener messageActionListener;

    private StringListener editListener;

    public void build(DBMessage message, boolean isSentByViewer) {
        if (isSentByViewer) {
            seenStatusLabel.setText(String.valueOf(message.getStatus().getStage()));
            senderTextLabel.setText(message.getText());
            senderUsernameLabel.setText(message.getUser().getUsername());
            DBUser user = message.getUser();
            if (!user.getImageInString().equals("") && user.getImageInString() != null) {
                byte[] imageInByte = Base64.getDecoder().decode(user.getImageInString());
                Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                senderProfileImage.setFill(new ImagePattern(image));
                senderProfileImage.setVisible(true);
            }
            receiverProfileImage.setVisible(false);
            receiverMessageImage.setVisible(false);
            if (message.getImageInString() != null) {
                if (!message.getImageInString().equals("")) {
                    byte[] imageInByte = Base64.getDecoder().decode(message.getImageInString());
                    Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                    SenderMessageImage.setFill(new ImagePattern(image));
                    SenderMessageImage.setVisible(true);
                }
            }
        } else {
            seenStatusLabel.setVisible(false);
            receiverTextLabel.setText(message.getText());
            receiverUserNameLabel.setText(message.getUser().getUsername());
            DBUser user = message.getUser();
            if (!user.getImageInString().equals("") && user.getImageInString() != null) {
                byte[] imageInByte = Base64.getDecoder().decode(user.getImageInString());
                Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                receiverProfileImage.setFill(new ImagePattern(image));
                receiverProfileImage.setVisible(true);
            }
            senderProfileImage.setVisible(false);
            SenderMessageImage.setVisible(false);
            if (message.getImageInString() != null) {
                if (!message.getImageInString().equals("")) {
                    byte[] imageInByte = Base64.getDecoder().decode(message.getImageInString());
                    Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
                    receiverMessageImage.setFill(new ImagePattern(image));
                    receiverMessageImage.setVisible(true);
                }
            }
        }
    }

    public void delete() {
        messageActionListener.listen("delete");
        editMessagePane.setVisible(false);
        editTextArea.setVisible(false);
    }

    public void edit() {
        editListener.listen(editTextArea.getText());
        editMessagePane.setVisible(false);
        editTextArea.setVisible(false);
    }

    public void showEdit() {
        messageActionListener.listen("showEdit");
    }

    public void showEditPane() {
        editTextArea.setVisible(true);
        editTextArea.setWrapText(true);
        editTextArea.setMaxWidth(190);
        editTextArea.setText(senderTextLabel.getText());
        editMessagePane.setVisible(true);
    }

    public void setMessageActionListener(StringListener messageActionListener) {
        this.messageActionListener = messageActionListener;
    }

    public void setRequestListener(StringListener editListener) {
        this.editListener = editListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        senderTextLabel.setWrapText(true);
        senderTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        senderTextLabel.setMaxWidth(Sizes.message());
        receiverTextLabel.setWrapText(true);
        receiverTextLabel.setTextAlignment(TextAlignment.JUSTIFY);
        receiverTextLabel.setMaxWidth(Sizes.message());
        editMessagePane.setVisible(false);
        editTextArea.setVisible(false);
        receiverMessageImage.setVisible(false);
        SenderMessageImage.setVisible(false);
    }
}
