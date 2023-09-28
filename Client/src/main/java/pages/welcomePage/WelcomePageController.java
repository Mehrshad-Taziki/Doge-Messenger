package pages.welcomePage;

import enums.Page;
import pages.mainPage.listener.PageListener;

public class WelcomePageController {
    private PageListener pageListener;

    public void login() {
        pageListener.listen(Page.Login);
    }

    public void register() {
        pageListener.listen(Page.Register);
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }
}
