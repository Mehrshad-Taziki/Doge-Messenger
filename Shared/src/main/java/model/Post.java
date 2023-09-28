package model;

import interfaces.ImageContainer;
import tools.ID;

import java.util.ArrayList;
import java.util.HashSet;

public class Post implements ImageContainer {
    public static final HashSet<Post> loadedPosts = new HashSet<>();
    private final int id;
    private final User writer;
    private final String text;
    private final DateTime dateTime;
    private final ArrayList<User> reports;
    private final String provider;
    private final boolean isComment;
    private String imageInString;
    private int score;
    private ArrayList<Post> comments;
    private ArrayList<User> likes;
    private int imageID;

    public Post(User writer, String text, boolean isComment, int imageID) {
        this.writer = writer;
        this.text = text.replaceAll("\\s+$", "");
        this.id = ID.generateNewID();
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.reports = new ArrayList<>();
        dateTime = new DateTime();
        loadedPosts.add(this);
        this.score = 0;
        this.provider = writer.getUsername();
        this.isComment = isComment;
        this.imageID = imageID;
        this.imageInString = "";
    }

    public Post(User writer, String text, int id, DateTime dateTime, String provider, boolean isComment, int imageID) {
        this.id = id;
        this.writer = writer;
        this.text = text.replaceAll("\\s+$", "");
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.dateTime = dateTime;
        loadedPosts.add(this);
        this.score = 0;
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.isComment = isComment;
        this.provider = provider;
        this.imageID = imageID;
        this.imageInString = "";
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImageInString() {
        return this.imageInString;
    }

    public void setImageInString(String imageInString) {
        this.imageInString = imageInString;
    }

    public String getProvider() {
        return provider;
    }

    public ArrayList<User> getReports() {
        return reports;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public User getWriter() {
        return writer;
    }

    public String getText() {
        return text;
    }

    public boolean isComment() {
        return isComment;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public ArrayList<User> getLikes() {
        return likes;
    }

    public void addComment(Post post) {
        this.comments.add(post);
    }

    public ArrayList<Post> getComments() {
        return comments;
    }

    public Post getReposted(String rePoster) {
        Post post = new Post(writer, text, ID.generateNewID(), dateTime, rePoster, false, imageID);
        post.setImageInString(imageInString);
        return post;
    }

    @Override
    public String toString() {
        return text;
    }
}
