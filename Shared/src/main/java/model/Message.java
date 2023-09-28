package model;

import enums.SeenStatus;
import interfaces.ImageContainer;

public class Message implements ImageContainer {
    private final User user;
    private final int id;
    private boolean deleted;
    private int imageID;
    private String imageInString;
    private String text;
    private SeenStatus status;

    public Message(User user, String text, boolean deleted, int id, SeenStatus status) {
        this.user = user;
        this.text = text.replaceAll("\\s+$", "");
        this.deleted = deleted;
        this.id = id;
        this.status = status;
        this.imageInString = "";
    }

    public User getUser() {
        return user;
    }

    public String getImageInString() {
        return imageInString;
    }

    public void setImageInString(String imageInString) {
        this.imageInString = imageInString;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void delete() {
        this.deleted = true;
        this.imageID = 0;
        this.text = "This Message Has Been Deleted";
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getId() {
        return id;
    }

    public SeenStatus getStatus() {
        return status;
    }

    public void setStatus(SeenStatus status) {
        if (this.status.getStage() < status.getStage()) {
            this.status = status;
        }
    }
}
