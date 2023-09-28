package pages.mainPage.listener;

import enums.Page;
import logics.LogicalAgent;
import requests.*;

public class MainPageListener implements PageListener {
    LogicalAgent logicalAgent;

    public MainPageListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(Page page) {
        if (page == Page.PERSONAL) {
            logicalAgent.addRequest(new PersonalPageRequest());
        }
        if (page == Page.EXPLORE) {
            logicalAgent.addRequest(new ExploreRequest());
        }
        if (page == Page.TIMELINE) {
            logicalAgent.addRequest(new TimeLineRequest());
        }
        if (page == Page.SETTING) {
            logicalAgent.addRequest(new SettingPageRequest());
        }
        if (page == Page.CHATBOX) {
            logicalAgent.addRequest(new ChatBoxRequest());
        }
        if (page == Page.ReConnect){
            logicalAgent.reConnect();
        }
    }
}
