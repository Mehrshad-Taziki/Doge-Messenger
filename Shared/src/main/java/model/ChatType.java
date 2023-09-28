package model;

import enums.ChatContainerType;

public class ChatType {
    private ChatContainerType type;
    private String id;

    public ChatType() {
        type = ChatContainerType.NONE;
        id = "";
    }

    public ChatContainerType getType() {
        return type;
    }

    public void setType(ChatContainerType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
