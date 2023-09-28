package database.saver;
import config.Config;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class PostSaver implements Saver<Post> {
    private static final Logger logger = LogManager.getLogger(PostSaver.class);
    private static final Config databaseConfig = Config.getConfig("database");

    private static File getPostFile(long postId) {
        logger.info("media/post " + postId + " File Opened");
        String path = databaseConfig.getProperty(String.class, "posts");
        File file = new File(path);
        File[] posts = file.listFiles();
        assert posts != null;
        for (File post : posts) {
            if (Integer.parseInt(post.getName()) == postId) {
                return post;
            }
        }
        return null;
    }

    public static void saveComments(Post post) {
        try {
            logger.info("File Of Post " + post.getId() + " CommentFile Saved");
            File postFile = getPostFile(post.getId());
            assert postFile != null;
            File comments = new File(postFile.getPath() + "\\" + "comments.txt");
            PrintStream printStream = new PrintStream(comments);
            printStream.println("Comments[ " + post.getComments().size() + " ]");
            for (int i = 0; i < post.getComments().size(); i++) {
                printStream.println(post.getComments().get(i).getId());
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Comments Of Post : " + post.getId() + " Failed \n" + e.getMessage());
        }

    }

    public static void saveLikes(Post post) {
        try {
            logger.info("File Of Post " + post.getId() + " LikedFile Saved");
            File postFile = getPostFile(post.getId());
            assert postFile != null;
            File likes = new File(postFile.getPath() + "\\" + "likes.txt");
            PrintStream printStream = new PrintStream(likes);
            printStream.println("Likes[ " + post.getLikes().size() + " ]");
            for (int i = 0; i < post.getLikes().size(); i++) {
                printStream.println(post.getLikes().get(i).getUsername());
            }
            printStream.println("Reports[ " + post.getReports().size() + " ]");
            for (int i = 0; i < post.getReports().size(); i++) {
                printStream.println(post.getReports().get(i).getUsername());
            }
            printStream.flush();
            printStream.close();
        } catch (FileNotFoundException e) {
            logger.warn("Saving Likes Of Post : " + post.getId() + " Failed \n" + e.getMessage());
        }
    }

    @Override
    public void save(Post post) {
        File postFile = getPostFile(post.getId());
        try {
            if (postFile == null) {
                logger.info("File Of Post " + post.getId() + " File Created");
                String path = databaseConfig.getProperty(String.class, "posts");
                path += "\\" + post.getId();
                postFile = new File(path);
                postFile.mkdir();
                File info = new File(path + "\\" + "info.txt");
                info.createNewFile();
                File comments = new File(path + "\\" + "comments.txt");
                comments.createNewFile();
                File likes = new File(path + "\\" + "likes.txt");
                likes.createNewFile();
            }
        } catch (IOException e) {
            logger.warn("Creating File For Post : " + post.getId() + " Failed \n" + e.getMessage());
        }
        try {
            logger.info("File Of Post " + post.getId() + " File Saved");
            File info = new File(postFile.getPath() + "\\" + "info.txt");
            PrintStream printStream = new PrintStream(info);
            printStream.println(post.getImageID());
            printStream.println(post.getWriter().getUsername());
            printStream.println(post.getProvider());
            printStream.println(post.isComment());
            printStream.println(post.getDateTime().toString());
            printStream.println(post.getText());
            saveComments(post);
            saveLikes(post);
            printStream.flush();
            printStream.close();
        } catch (IOException e) {
            logger.warn("Saving Post : " + post.getId() + " Failed \n" + e.getMessage());
        }
    }
}