package media.listener;

import enums.PostActions;
import logics.LogicalAgent;
import requests.PostActionRequest;
import listener.PostActionListener;
import media.comment.logic.CommentLogic;

public class CommentMediaListener implements PostActionListener {
    CommentLogic logic;
    LogicalAgent logicalAgent;

    public CommentMediaListener(CommentLogic logic, LogicalAgent logicalAgent) {
        this.logic = logic;
        this.logicalAgent = logicalAgent;
    }

    @Override
    public void listen(PostActions action) {
        switch (action) {
            case NEXT -> logic.nextComment();
            case PREVIOUS -> logic.previousComment();
            case SEECOMMENTS -> logicalAgent.addRequest(new PostActionRequest(logic.currentComment().getId(), action));
        }
    }
}
