package media.listener;

import enums.PostActions;
import logics.LogicalAgent;
import requests.PostActionRequest;
import listener.PostActionListener;
import media.post.logic.PostLogic;

public class PostMediaActionListener implements PostActionListener {
    LogicalAgent logicalAgent;
    PostLogic logic;

    public PostMediaActionListener(LogicalAgent logicalAgent, PostLogic logic) {
        this.logicalAgent = logicalAgent;
        this.logic = logic;
    }

    @Override
    public void listen(PostActions action) {
        switch (action) {
            case NEXT -> logic.nextPost();
            case PREVIOUS -> logic.previousPost();
            default -> logicalAgent.addRequest(new PostActionRequest(logic.currentPost().getId(), action));
        }
    }
}
