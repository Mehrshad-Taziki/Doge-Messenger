package network.handler;

public class Handler {
    private ChatBoxHandler chatBoxHandler;
    private ExploreHandler exploreHandler;
    private PersonalHandler personalHandler;
    private PostHandler postHandler;
    private ProfileHandler profileHandler;
    private SettingHandler settingHandler;
    private TimeLineHandler timeLineHandler;
    private AuthHandler authHandler;

    public ChatBoxHandler chatBoxHandler() {
        if (chatBoxHandler == null) {
            chatBoxHandler = new ChatBoxHandler();
        }
        return chatBoxHandler;
    }

    public ExploreHandler exploreHandler() {
        if (exploreHandler == null) {
            exploreHandler = new ExploreHandler();
        }
        return exploreHandler;
    }

    public PersonalHandler personalHandler() {
        if (personalHandler == null) {
            personalHandler = new PersonalHandler();
        }
        return personalHandler;
    }

    public PostHandler postHandler() {
        if (postHandler == null) {
            postHandler = new PostHandler();
        }
        return postHandler;
    }

    public ProfileHandler profileHandler() {
        if (profileHandler == null) {
            profileHandler = new ProfileHandler();
        }
        return profileHandler;
    }

    public SettingHandler settingHandler() {
        if (settingHandler == null) {
            settingHandler = new SettingHandler();
        }
        return settingHandler;
    }

    public TimeLineHandler timeLineHandler() {
        if (timeLineHandler == null) {
            timeLineHandler = new TimeLineHandler();
        }
        return timeLineHandler;
    }

    public AuthHandler authHandler() {
        if (authHandler == null) {
            authHandler = new AuthHandler();
        }
        return authHandler;
    }
}
