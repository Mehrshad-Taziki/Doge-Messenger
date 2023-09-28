package listener;

import enums.UserAction;

public interface UserActionListener {
    void listen(UserAction action, String username);
}
