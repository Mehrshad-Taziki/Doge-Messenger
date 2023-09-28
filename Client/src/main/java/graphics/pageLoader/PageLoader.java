package graphics.pageLoader;

import logics.LogicalAgent;

public class PageLoader {
    private static PageLoader pageLoader;
    private static LogicalAgent logicalAgent;
    private PersonalPageLoader personalPageLoader;
    private TimeLinePageLoader timeLinePageLoader;
    private ExplorePageLoader explorePageLoader;
    private ChatBoxLoader chatBoxLoader;
    private SettingLoader settingLoader;

    public PageLoader(LogicalAgent logicalAgent) {
        PageLoader.logicalAgent = logicalAgent;
    }

    public static PageLoader get() {
        if (pageLoader == null) {
            pageLoader = new PageLoader(logicalAgent);
        }
        return pageLoader;
    }

    public PersonalPageLoader getPersonalPageLoader() {
        if (personalPageLoader == null) {
            personalPageLoader = new PersonalPageLoader(logicalAgent);
        }
        return personalPageLoader;
    }

    public TimeLinePageLoader getTimeLinePageLoader() {
        if (timeLinePageLoader == null) {
            timeLinePageLoader = new TimeLinePageLoader(logicalAgent);
        }
        return timeLinePageLoader;
    }

    public ExplorePageLoader getExplorePageLoader() {
        if (explorePageLoader == null) {
            explorePageLoader = new ExplorePageLoader(logicalAgent);
        }
        return explorePageLoader;
    }

    public ChatBoxLoader getChatBoxLoader() {
        if (chatBoxLoader == null) {
            chatBoxLoader = new ChatBoxLoader(logicalAgent);
        }
        return chatBoxLoader;
    }

    public SettingLoader getSettingLoader() {
        if (settingLoader == null) {
            settingLoader = new SettingLoader(logicalAgent);
        }
        return settingLoader;
    }

}
