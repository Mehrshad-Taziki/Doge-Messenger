package model.DB;

import model.DateTime;
import model.Notification;
import model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class DBUser {
    private final int id;
    private final String username;
    private final ArrayList<DBPost> posts;
    private final ArrayList<DBPost> liked;
    private final LinkedList<Notification> notification;
    private final String email;
    private final String name;
    private final String lastName;
    private final DateTime birthDay;
    private final DateTime lastSeen;
    private final String phoneNumber;
    private final String biography;
    private final boolean active;
    private final int imageId;
    private final String imageInString;
    private boolean isPublic;
    private int lastSeenType;
    private boolean publicNumber;
    private boolean publicEmail;
    private boolean publicBirthday;


    public DBUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.posts = new ArrayList<>();
        user.getPosts().forEach(post -> this.posts.add(new DBPost(post)));
        this.liked = new ArrayList<>();
        user.getLiked().forEach(post -> this.liked.add(new DBPost(post)));
        this.notification = user.getNotification();
        this.email = user.getEmail();
        this.birthDay = user.getBirthDay();
        this.lastSeen = user.getLastSeen();
        this.phoneNumber = user.getPhoneNumber();
        this.biography = user.getBiography();
        this.isPublic = user.isPublic();
        this.active = user.isActive();
        this.publicBirthday = user.isPublicBirthday();
        this.publicEmail = user.isPublicEmail();
        this.publicNumber = user.isPublicNumber();
        this.lastSeenType = user.getLastSeenType();
        this.imageId = user.getImageID();
        this.imageInString = user.getImageInString();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<DBPost> getPosts() {
        return posts;
    }

    public ArrayList<DBPost> getLiked() {
        return liked;
    }

    public LinkedList<Notification> getNotification() {
        return notification;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public DateTime getBirthDay() {
        return birthDay;
    }

    public DateTime getLastSeen() {
        return lastSeen;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBiography() {
        return biography;
    }

    public String getImageInString() {
        return imageInString;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getLastSeenType() {
        return lastSeenType;
    }

    public void setLastSeenType(int lastSeenType) {
        this.lastSeenType = lastSeenType;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPublicNumber() {
        return publicNumber;
    }

    public void setPublicNumber(boolean publicNumber) {
        this.publicNumber = publicNumber;
    }

    public boolean isPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(boolean publicEmail) {
        this.publicEmail = publicEmail;
    }

    public boolean isPublicBirthday() {
        return publicBirthday;
    }

    public void setPublicBirthday(boolean publicBirthday) {
        this.publicBirthday = publicBirthday;
    }

    public int getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBUser)) return false;
        DBUser dbUser = (DBUser) o;
        return Objects.equals(username, dbUser.username);
    }
}
