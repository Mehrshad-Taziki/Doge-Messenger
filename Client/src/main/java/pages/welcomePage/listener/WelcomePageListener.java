package pages.welcomePage.listener;

import enums.Page;
import logics.LogicalAgent;
import pages.mainPage.listener.PageListener;

public class WelcomePageListener implements PageListener {
    LogicalAgent logicalAgent;

    public WelcomePageListener(LogicalAgent logicalAgent) {
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(Page page) {
        if (page == Page.Login){
            logicalAgent.switchToLoginForm();
        }
        if (page == Page.Register){
            logicalAgent.switchToRegisterForm();
        }
    }
}
