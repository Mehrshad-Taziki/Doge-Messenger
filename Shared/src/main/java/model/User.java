package model;

import interfaces.ImageContainer;
import tools.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class User implements ImageContainer {
    private final int id;
    private final String username;
    private final ArrayList<Post> posts;
    private final ArrayList<Post> liked;
    private final ArrayList<User> followings;
    private final ArrayList<User> followers;
    private final ArrayList<User> blocked;
    private final ArrayList<User> muted;
    private final ArrayList<String> requests;
    private final LinkedList<Notification> notification;
    private final HashMap<String, PrivateChat> privateChats;
    private final HashMap<Integer, Group> groups;
    private final SavedMessage savedMessage;
    private final HashMap<String, Contact> contacts;
    private String password;
    private String email;
    private String name;
    private String lastName;
    private DateTime birthDay;
    private DateTime lastSeen;
    private String phoneNumber;
    private String biography;
    private boolean isPublic;
    private int lastSeenType;
    private boolean active;
    private boolean publicNumber;
    private boolean publicEmail;
    private boolean publicBirthday;
    private int imageId;
    private String imageInString;

    private User(int id, String passWord, String eMail, String userName, String name, String lastName, DateTime birthDay, String phoneNumber, String biography, int imageId) {
        this.password = passWord;
        this.email = eMail;
        this.username = userName;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.biography = biography;
        this.posts = new ArrayList<>();
        this.liked = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.blocked = new ArrayList<>();
        this.muted = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.notification = new LinkedList<>();
        this.privateChats = new HashMap<>();
        this.groups = new HashMap<>();
        this.contacts = new HashMap<>();
        this.savedMessage = new SavedMessage(username);
        this.lastSeenType = 1;
        this.isPublic = true;
        this.active = true;
        this.lastSeen = new DateTime();
        this.publicEmail = true;
        this.publicNumber = true;
        this.publicBirthday = true;
        this.imageId = imageId;
        this.imageInString = "";
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ArrayList<Post> getLiked() {
        return liked;
    }

    public void addPost(Post post) {
        this.posts.add(0, post);
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public DateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(DateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getImageInString() {
        return imageInString;
    }

    public void setImageInString(String imageInString) {
        this.imageInString = imageInString;
    }

    public DateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(DateTime birthDay) {
        this.birthDay = birthDay;
    }

    public boolean isPublicBirthday() {
        return publicBirthday;
    }

    public void setPublicBirthday(boolean publicBirthday) {
        this.publicBirthday = publicBirthday;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LinkedList<Notification> getNotification() {
        return notification;
    }

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public HashMap<String, Group> getGroupsByName() {
        HashMap<String, Group> groupsByName = new HashMap<>();
        groups.forEach((ID, group) -> groupsByName.put(group.getGroupName(), group));
        return groupsByName;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, Contact> getContacts() {
        return contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;

    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<User> getFollowings() {
        return followings;
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

    public ArrayList<User> getMuted() {
        return muted;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public ArrayList<User> getBlocked() {
        return blocked;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public int getLastSeenType() {
        return lastSeenType;
    }

    public void setLastSeenType(int lastSeenType) {
        this.lastSeenType = lastSeenType;
    }

    public HashMap<String, PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public void updateLastSeen() {
        this.lastSeen = new DateTime();
    }

    public SavedMessage getSavedMessage() {
        return savedMessage;
    }

    @Override
    public int getImageID() {
        return imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    public static class UserBuilder {
        int id = 0;
        String passWord;
        String email;
        String userName;
        String name;
        String lastName;
        DateTime birthDay = new DateTime(0, 0, 0, 0, 0);
        String phoneNumber;
        String biography = "-";
        int imageID = 0;


        public UserBuilder setPassWord(String passWord) {
            this.passWord = passWord;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setBirthDay(DateTime birthDay) {
            this.birthDay = birthDay;
            return this;
        }

        public UserBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder setBiography(String biography) {
            this.biography = biography;
            return this;
        }

        public UserBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public UserBuilder setImageID(int id) {
            this.imageID = id;
            return this;
        }

        public User build() {
            if (this.id == 0) {
                this.id = ID.generateNewID();
            }
            return new User(this.id, this.passWord, this.email, this.userName, this.name, this.lastName, this.birthDay, this.phoneNumber, this.biography, imageID);
        }
    }
}