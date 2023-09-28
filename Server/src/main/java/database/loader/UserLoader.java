package database.loader;

import config.Config;
import database.DBSet.Context;
import database.Images;
import database.Users;
import enums.SeenStatus;
import javafx.embed.swing.SwingFXUtils;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tools.ImageHandler;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class UserLoader implements Loader<User> {
    private static final Logger logger = LogManager.getLogger(UserLoader.class);
    private static final Config databaseConfig = Config.getConfig("database");

    public void loadChats(User user) {
        try {
            String usersPath = databaseConfig.getProperty(String.class, "users");
            String path = usersPath + "\\" + user.getId() + "\\" + "massage";
            File userChats = new File(path);
            for (File userChat :
                    Objects.requireNonNull(userChats.listFiles())) {
                Scanner reader = new Scanner(userChat);
                int unreadMessage = reader.nextInt();
                reader.nextLine();
                String secondUsername = reader.nextLine();
                if (Context.get().Users.get(secondUsername) == null) continue;
                PrivateChat privateChat = new PrivateChat(user, Context.get().Users.get(secondUsername));
                privateChat.setUnreadMessages(unreadMessage);
                while (reader.hasNextInt()) {
                    StringBuilder text = new StringBuilder();
                    int id = reader.nextInt();
                    int imageID = reader.nextInt();
                    SeenStatus status = SeenStatus.getStatus(reader.nextInt());
                    reader.nextLine();
                    String username = reader.nextLine();
                    boolean isDeleted = reader.nextBoolean();
                    reader.nextLine();
                    while (true) {
                        String line = reader.nextLine();
                        if (line.equals("-")) {
                            if (Context.get().Users.get(username) == null) break;
                            Message message = new Message(Context.get().Users.get(username), text.toString(), isDeleted, id, status);
                            message.setImageID(imageID);
                            String imageInString = "";
                            if (imageID != 0) {
                                imageInString = Base64.getEncoder().encodeToString(ImageHandler.toByteArray(SwingFXUtils.fromFXImage(Images.get(imageID), null), "png"));
                            }
                            message.setImageInString(imageInString);
                            privateChat.getMessages().add(message);
                            break;
                        } else {
                            text.append(line).append('\n');
                        }
                    }
                }
                reader.close();
                user.getPrivateChats().put(secondUsername, privateChat);
            }
            logger.info("Chats Of " + user.getUsername() + '/' + user.getId() + " Loaded");
        } catch (IOException e) {
            logger.warn("Loading Chats Of " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }

    }

    public void loadSavedMessage(User user) {
        try {
            System.out.println("Da man saved Message");
            String usersPath = databaseConfig.getProperty(String.class, "users");
            String path = usersPath + "\\" + user.getId() + "\\" + "savedMessage.txt";
            File userChat = new File(path);
            Scanner reader = new Scanner(userChat);
            int unreadMessage = reader.nextInt();
            reader.nextLine();
            String Username = reader.nextLine();
            while (reader.hasNextInt()) {
                System.out.println("da?????");
                StringBuilder text = new StringBuilder();
                int id = reader.nextInt();
                int imageID = reader.nextInt();

                reader.nextLine();
                String username = reader.nextLine();
                boolean isDeleted = reader.nextBoolean();
                reader.nextLine();
                while (true) {
                    String line = reader.nextLine();
                    if (line.equals("-")) {
                        if (Context.get().Users.get(username) == null) break;
                        Message message = new Message(Context.get().Users.get(username), text.toString(), isDeleted, id, SeenStatus.SENT);
                        message.setImageID(imageID);
                        String imageInString = "";
                        if (imageID != 0) {
                            imageInString = Base64.getEncoder().encodeToString(ImageHandler.toByteArray(SwingFXUtils.fromFXImage(Images.get(imageID), null), "png"));
                        }
                        message.setImageInString(imageInString);
                        user.getSavedMessage().getMessages().add(message);
                        break;
                    } else {
                        text.append(line).append('\n');
                    }
                }
            }
            reader.close();
            logger.info("SavedMessage Of " + user.getUsername() + '/' + user.getId() + " Loaded");
        } catch (IOException e) {
            logger.warn("Loading SavedMessage Of " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }

    }

    public void loadGroups(User user) {
        try {
            String usersPath = databaseConfig.getProperty(String.class, "users");
            String path = usersPath + "\\" + user.getId() + "\\" + "groups";
            File userChats = new File(path);
            for (File userChat :
                    Objects.requireNonNull(userChats.listFiles())) {
                Scanner reader = new Scanner(userChat);
                int groupID = reader.nextInt();
                int unreadMessage = reader.nextInt();
                reader.nextLine();
                String name = reader.nextLine();
                reader.next();
                int count = reader.nextInt();
                reader.nextLine();
                HashSet<User> members = new HashSet<>();
                for (int i = 0; i < count; i++) {
                    String username = reader.nextLine();
                    if (Users.userExists(username)) {
                        members.add(Context.get().Users.get(username));
                    }
                }
                Group group = new Group(name, user, groupID, members);
                group.setUnreadMessages(unreadMessage);
                while (reader.hasNextInt()) {
                    StringBuilder text = new StringBuilder();
                    int id = reader.nextInt();
                    int imageID = reader.nextInt();
                    reader.nextLine();
                    String username = reader.nextLine();
                    boolean isDeleted = reader.nextBoolean();
                    reader.nextLine();
                    while (true) {
                        String line = reader.nextLine();
                        if (line.equals("-")) {
                            if (Context.get().Users.get(username) == null) break;
                            Message message = new Message(Context.get().Users.get(username), text.toString(), isDeleted, id, SeenStatus.SENT);
                            message.setImageID(imageID);
                            String imageInString = "";
                            if (imageID != 0) {
                                imageInString = Base64.getEncoder().encodeToString(ImageHandler.toByteArray(SwingFXUtils.fromFXImage(Images.get(imageID), null), "png"));
                            }
                            message.setImageInString(imageInString);
                            group.getMessages().add(message);
                            break;
                        } else {
                            text.append(line).append('\n');
                        }
                    }
                }
                reader.close();
                user.getGroups().put(groupID, group);
            }
            logger.info("Groups Of " + user.getUsername() + '/' + user.getId() + " Loaded");

        } catch (IOException e) {
            logger.warn("Loading Groups Of " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }
    }

    public void loadPosts(User user) {
        try {

            String path = databaseConfig.getProperty(String.class, "users");
            File userPosts = new File(path + "\\" + user.getId() + "\\" + "posts.txt");
            Scanner scanner = new Scanner(userPosts);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                String postId = scanner.next();
                Post post = Context.get().Posts.get(postId);
                if (post != null) {
                    user.getPosts().add(post);
                }
            }
            scanner.close();
            logger.info("Posts Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");

        } catch (IOException e) {
            logger.warn("Loading Posts Of User " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }

    }

    public void loadFollower(User user) {
        try {
            String path = databaseConfig.getProperty(String.class, "users");
            File userFollowers = new File(path + "\\" + user.getId() + "\\" + "followers.txt");
            Scanner scanner = new Scanner(userFollowers);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                User follower = Context.get().Users.get(scanner.next());
                if (follower != null) {
                    user.getFollowers().add(follower);
                }
            }
            scanner.next();
            count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                String requester = scanner.next();
                if (requester != null) {
                    user.getRequests().add(requester);
                }
            }
            logger.info("Followers Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");

            scanner.close();
        } catch (IOException e) {
            logger.warn("Loading Followers Of User " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }

    }

    public void loadFollowing(User user) {
        try {
            String path = databaseConfig.getProperty(String.class, "users");
            File userFollowings = new File(path + "\\" + user.getId() + "\\" + "followings.txt");
            Scanner scanner = new Scanner(userFollowings);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                User following = Context.get().Users.get(scanner.next());
                if (following != null) {
                    user.getFollowings().add(following);
                }
            }
            scanner.close();
            logger.info("Followings Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");

        } catch (IOException e) {
            logger.warn("Loading Followings Of User " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }

    }

    public void loadBlackList(User user) {
        try {
            String path = databaseConfig.getProperty(String.class, "users");
            File userBlocked = new File(path + "\\" + user.getId() + "\\" + "blacklist.txt");
            Scanner scanner = new Scanner(userBlocked);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                User blocked = Context.get().Users.get(scanner.next());
                if (blocked != null) {
                    user.getBlocked().add(blocked);
                }
            }
            scanner.next();
            count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                User muted = Context.get().Users.get(scanner.next());
                if (muted != null) {
                    user.getMuted().add(muted);
                }
            }
            logger.info("BlackList Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");

            scanner.close();
        } catch (IOException e) {
            logger.warn("BlackList Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");
        }

    }

    public void loadContact(User user) {
        try {
            String path = databaseConfig.getProperty(String.class, "users");
            File userGroups = new File(path + "\\" + user.getId() + "\\" + "contacts.txt");
            Scanner scanner = new Scanner(userGroups);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                String name = scanner.next();
                int membersCount = scanner.nextInt();
                HashSet<User> members = new HashSet<>();
                for (int j = 0; j < membersCount; j++) {
                    String username = scanner.next();
                    if (Users.userExists(username))
                        members.add(Context.get().Users.get(username));
                }
                Contact contact = new Contact(name, user, members);
                user.getContacts().put(name, contact);
            }
            scanner.close();
            logger.info("Groups Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");
        } catch (IOException e) {
            logger.warn("Loading Groups Of User " + user.getUsername() + '/' + user.getId() + " Failed\n ");
        }

    }

    public void loadLiked(User user) {
        try {
            String path = databaseConfig.getProperty(String.class, "users");
            File userLiked = new File(path + "\\" + user.getId() + "\\" + "liked.txt");
            Scanner scanner = new Scanner(userLiked);
            scanner.next();
            int count = scanner.nextInt();
            scanner.next();
            for (int i = 0; i < count; i++) {
                String postId = scanner.next();
                Post post = Context.get().Posts.get(postId);
                if (post != null) {
                    user.getLiked().add(post);
                }
            }
            scanner.close();
            logger.info("LikedPosts Of User " + user.getUsername() + '/' + user.getId() + " File Loaded");
        } catch (IOException e) {
            logger.warn("Loading LikedPosts Of User " + user.getUsername() + '/' + user.getId()
                    + " Failed\n ");
        }

    }

    @Override
    public User load(String userName) {
        if (!Users.userExists(userName)) {
            return null;
        }
        try {
            String path = Users.getUserFile(userName).getPath();
            path += "\\" + "info.txt";
            File userInfo = new File(path);
            Scanner loaderScanner = new Scanner(userInfo);
            loaderScanner.next();
            int imageId = loaderScanner.nextInt();
            loaderScanner.next();
            int lastSeenType = loaderScanner.nextInt();
            loaderScanner.next();
            boolean isActive = loaderScanner.nextBoolean();
            loaderScanner.next();
            boolean isPublic = loaderScanner.nextBoolean();
            loaderScanner.next();
            int id = loaderScanner.nextInt();
            loaderScanner.next();
            String username = loaderScanner.next();
            loaderScanner.next();
            String name = loaderScanner.next();
            String lastName = loaderScanner.next();
            loaderScanner.next();
            int year = loaderScanner.nextInt();
            int month = loaderScanner.nextInt();
            int day = loaderScanner.nextInt();
            loaderScanner.next();
            loaderScanner.next();
            DateTime birthday = new DateTime(year, month, day, 0, 0);
            loaderScanner.next();
            String email = loaderScanner.next();
            loaderScanner.next();
            String phoneNumber = loaderScanner.next();
            loaderScanner.next();
            String password = loaderScanner.next();
            loaderScanner.next();
            loaderScanner.nextLine();
            String biography = loaderScanner.nextLine();
            loaderScanner.next();
            DateTime lastSeen = new DateTime(loaderScanner.nextInt(), loaderScanner.nextInt(),
                    loaderScanner.nextInt(), loaderScanner.nextInt(), loaderScanner.nextInt());
            loaderScanner.next();
            boolean publicEmail = loaderScanner.nextBoolean();
            loaderScanner.next();
            boolean publicNumber = loaderScanner.nextBoolean();
            loaderScanner.next();
            boolean publicBirthday = loaderScanner.nextBoolean();
            User.UserBuilder builder = new User.UserBuilder();
            builder.setPassWord(password).setUserName(username).setEmail(email).setName(name).setLastName(lastName).setPhoneNumber(phoneNumber).setBiography(biography).setBirthDay(birthday).setId(id).setImageID(imageId);
            User helper = builder.build();
            loaderScanner.next();
            int count = loaderScanner.nextInt();
            loaderScanner.next();
            for (int i = 0; i < count; i++) {
                int type = loaderScanner.nextInt();
                String sender = loaderScanner.next();
                if (Users.userExists(sender)) {
                    helper.getNotification().add(Notification.generateNotification(type, sender));
                }
            }
            String imageInString = "";
            if (imageId != 0) {
                imageInString = Base64.getEncoder().encodeToString(ImageHandler.toByteArray(SwingFXUtils.fromFXImage(Images.get(imageId), null), "png"));
            }
            helper.setImageInString(imageInString);
            System.out.println("stage1");
            Context.get().Users.getAll().add(helper);
            System.out.println("stage2");
            loadPosts(helper);
            loadBlackList(helper);
            loadFollower(helper);
            loadFollowing(helper);
            loadLiked(helper);
            loadChats(helper);
            loadGroups(helper);
            loadSavedMessage(helper);
            loadContact(helper);
            helper.setLastSeen(lastSeen);
            helper.setPublic(isPublic);
            helper.setPublicEmail(publicEmail);
            helper.setPublicNumber(publicNumber);
            helper.setPublicBirthday(publicBirthday);
            helper.setActive(isActive);
            helper.setLastSeenType(lastSeenType);
            loaderScanner.close();
            logger.info("Loading User " + userName + '/' + Users.getID(userName) + " Completed!");
            return helper;
        } catch (Throwable e) {
            logger.warn("Loading User " + userName + '/' + Users.getID(userName) + " Failed\n ");
            return null;
        }
    }

    @Override
    public File getFile(String userName) {
        int id = Users.getID(userName);
        String path = databaseConfig.getProperty(String.class, "users");
        File file = new File(path);
        File[] users = file.listFiles();
        assert users != null;
        for (File user : users) {
            if (user.getName().equals(String.valueOf(id))) {
                return user;
            }
        }
        return null;
    }
}
