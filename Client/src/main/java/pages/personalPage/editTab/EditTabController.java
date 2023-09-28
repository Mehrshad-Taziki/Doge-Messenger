package pages.personalPage.editTab;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import listener.RequestListener;
import model.DB.DBUser;
import requests.ChangeProfilePhotoRequest;
import requests.EditProfileRequest;
import tools.ImageHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.Base64;

public class EditTabController {
    BufferedImage profileBufferedImage;
    private RequestListener listener;
    @FXML
    private CheckBox emailCheckBox;
    @FXML
    private CheckBox numberCheckBox;
    @FXML
    private CheckBox birthdayCheckBox;
    @FXML
    private DatePicker birthdayPicker;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField biographyTextField;
    @FXML
    private Circle profileImageView;

    public void build(DBUser user) {
        if (!user.getImageInString().equals("") && user.getImageInString() != null) {
            byte[] imageInByte = Base64.getDecoder().decode(user.getImageInString());
            Image image = SwingFXUtils.toFXImage(ImageHandler.toBufferedImage(imageInByte), null);
            profileImageView.setFill(new ImagePattern(image));
            profileImageView.setVisible(true);
        }
        emailCheckBox.setSelected(user.isPublicEmail());
        birthdayCheckBox.setSelected(user.isPublicBirthday());
        numberCheckBox.setSelected(user.isPublicNumber());
        biographyTextField.setText(user.getBiography());
        emailTextField.setText(user.getEmail());
        nameTextField.setText(user.getName());
        lastnameTextField.setText(user.getLastName());
    }

    public void deletePhoto() {
        profileImageView.setFill(Color.BLUE);
        listener.listen(new ChangeProfilePhotoRequest(null));
    }

    public void submitPhoto() {
        if (profileBufferedImage != null)
            listener.listen(new ChangeProfilePhotoRequest(ImageHandler.toByteArray(profileBufferedImage, "png")));
        profileBufferedImage = null;
    }

    public void submit() {
        LocalDate birthday = birthdayPicker.getValue();
        EditProfileRequest request =
                new EditProfileRequest(emailTextField.getText(), nameTextField.getText(), lastnameTextField.getText(),
                        biographyTextField.getText(), birthday, emailCheckBox.isSelected(),
                        numberCheckBox.isSelected(), birthdayCheckBox.isSelected());
        listener.listen(request);
    }

    public void changeProfileImage() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().addAll(extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            profileBufferedImage = ImageIO.read(file);
            if (profileImageView != null)
                profileImageView.setFill(new ImagePattern(SwingFXUtils.toFXImage(profileBufferedImage, null)));
        } catch (Throwable ignored) {
        }
    }

    public void loadListener(RequestListener listener) {
        this.listener = listener;
    }

}