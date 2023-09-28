package database.saver;

import model.*;

public class MainSaver {
    static MainSaver mainSaver;
    public Saver<User> User;
    public Saver<Post> Post;
    public Saver<SavedMessage> SavedMessage;
    public Saver<PrivateChat> PrivateChat;
    public Saver<Group> Group;

    private MainSaver() {
        User = new UserSaver();
        Post = new PostSaver();
        SavedMessage = new SavedMessageSaver();
        PrivateChat = new PrivateChatSaver();
        Group = new GroupSaver();
    }

    public static MainSaver get() {
        if (mainSaver == null) {
            mainSaver = new MainSaver();
        }
        return mainSaver;
    }

}
