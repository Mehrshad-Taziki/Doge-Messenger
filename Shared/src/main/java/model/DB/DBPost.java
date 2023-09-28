package model.DB;

import model.DateTime;
import model.Post;

import java.util.ArrayList;

public class DBPost {
    private final int id;
    private final String writer;
    private final String text;
    private final DateTime dateTime;
    private final String provider;
    private final boolean isComment;
    private final int score;
    private final ArrayList<String> likes;
    private final String imageInString;
    private final String writerImageInString;

    public DBPost(Post post) {
        this.id = post.getId();
        this.writer = post.getWriter().getUsername();
        this.text = post.getText().replaceAll("\\s+$", "");
        this.provider = post.getProvider();
        this.dateTime = post.getDateTime();
        this.isComment = post.isComment();
        this.score = post.getScore();
        this.imageInString = post.getImageInString();
        this.writerImageInString = post.getWriter().getImageInString();
        this.likes = new ArrayList<>();
        post.getLikes().forEach(user -> this.likes.add(user.getUsername()));
    }

    public int getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getText() {
        return text;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isComment() {
        return isComment;
    }

    public int getScore() {
        return score;
    }

    public String getWriterImageInString() {
        return writerImageInString;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public String getImageInString() {
        return imageInString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DBPost)) return false;
        DBPost dbPost = (DBPost) o;
        return id == dbPost.id;
    }

}
