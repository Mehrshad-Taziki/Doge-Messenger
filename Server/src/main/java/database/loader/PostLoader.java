package database.loader;

import config.Config;
import database.DBSet.Context;
import database.Images;
import database.Users;
import javafx.embed.swing.SwingFXUtils;
import model.DateTime;
import model.Post;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tools.ImageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class PostLoader implements Loader<Post> {
    private static final Logger logger = LogManager.getLogger(PostLoader.class);
    private static final Config databaseConfig = Config.getConfig("database");

    public static void loadComments(Post post) {
        try {
            String path = databaseConfig.getProperty(String.class, "posts");
            File comments = new File(path + "\\" + post.getId() + "\\" + "comments.txt");
            Scanner scanner = new Scanner(comments);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                String postId = scanner.next();
                Post comment = Context.get().Posts.get(postId);
                if (comment != null) {
                    post.getComments().add(comment);
                }
            }
            logger.info("Comments Of Post : " + post.getId() + " Loaded");
            scanner.close();
        } catch (IOException e) {
            logger.info("Loading Comments of Post : " + post.getId() + " Failed\n ");
        }

    }

    public static void loadLikes(Post post) {
        try {
            String path = databaseConfig.getProperty(String.class, "posts");
            File likes = new File(path + "\\" + post.getId() + "\\" + "likes.txt");
            Scanner scanner = new Scanner(likes);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                User liker = Context.get().Users.get(scanner.next());
                if (liker != null) {
                    post.getLikes().add(liker);
                }
            }
            scanner.next();
            count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                User reporter = Context.get().Users.get(scanner.next());
                if (reporter != null) {
                    post.getReports().add(reporter);
                }
            }
            logger.info("Likes Of Post : " + post.getId() + " Loaded");
            scanner.close();
        } catch (IOException e) {
            logger.info("Loading Likes of Post : " + post.getId() + " Failed\n ");
        }

    }

    @Override
    public Post load(String ID) {
        int id = Integer.parseInt(ID);
        if (getFile(ID) == null) {
            return null;
        }
        try {
            String path = Objects.requireNonNull(getFile(ID)).getPath();
            path += "\\" + "info.txt";
            File post = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(post));
            String line;
            int imageId = Integer.parseInt(reader.readLine());
            String username = reader.readLine();
            String provider = reader.readLine();
            User user = Context.get().Users.get(username);
            if (user == null) {
                return null;
            }
            if (!Users.userExists(username)) {
                return null;
            }
            boolean isComment = Boolean.parseBoolean(reader.readLine());
            String[] date = reader.readLine().split(" ");
            DateTime dateTime = new DateTime(Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(date[3]), Integer.parseInt(date[4]), Integer.parseInt(date[5]));
            StringBuilder text = new StringBuilder(reader.readLine());
            try {
                while ((line = reader.readLine()) != null) {
                    text.append('\n').append(line);
                }
            } finally {
                reader.close();
            }
            Post post1 = new Post(user, text.toString(), id, dateTime, provider, isComment, imageId);
            String imageInString = "";
            if (imageId != 0) {
                imageInString = Base64.getEncoder().encodeToString(ImageHandler.toByteArray(SwingFXUtils.fromFXImage(Images.get(imageId), null), "png"));
            }
            post1.setImageInString(imageInString);
            loadComments(post1);
            loadLikes(post1);
            post1.setScore(post1.getLikes().size() * (-5) + post1.getReports().size() * 2);
            logger.info("Loading Post : " + id + " Completed!");
            Context.get().Posts.getAll().add(post1);
            return post1;
        } catch (IOException e) {
            logger.info("Loading Post : " + id + " Failed\n ");
            return null;
        }

    }

    @Override
    public File getFile(String ID) {
        int id = Integer.parseInt(ID);
        logger.info("media/post " + id + " File Opened");
        String path = databaseConfig.getProperty(String.class, "posts");
        File file = new File(path);
        File[] posts = file.listFiles();
        assert posts != null;
        for (File post : posts) {
            if (Integer.parseInt(post.getName()) == id) {
                return post;
            }
        }
        return null;
    }
}
