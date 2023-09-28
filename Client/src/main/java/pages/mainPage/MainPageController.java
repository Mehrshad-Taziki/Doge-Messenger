package pages.mainPage;

import enums.Page;
import listener.Listener;
import pages.mainPage.listener.PageListener;

public class MainPageController {

    private PageListener pageListener;

    private Listener listener;

    public void personal() {
        pageListener.listen(Page.PERSONAL);
    }

    public void explore() {
        pageListener.listen(Page.EXPLORE);
    }

    public void timeline() {
        pageListener.listen(Page.TIMELINE);
    }

    public void setting() {
        pageListener.listen(Page.SETTING);
    }

    public void chatBox() {
        pageListener.listen(Page.CHATBOX);
    }

    public void reConnect() {
        pageListener.listen(Page.ReConnect);
    }

    public void exit() {
        listener.listen();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }
}
