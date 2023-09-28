package listener;


import enums.PostActions;

public interface PostActionListener {
    void listen(PostActions action);
}
