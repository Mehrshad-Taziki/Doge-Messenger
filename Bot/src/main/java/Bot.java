public interface Bot {
    String getFields();

    void setFields(String data);

    String answerMessage(String text, int senderID);

    String sendMessage(int receiverID);

    String answerGPMessage(String text, int senderID, int groupID);

    String sendGPMessage(int groupID);

    String sendTweet(String text);

    String sendComment(String text, int senderID, int postID);

    boolean invokeAnswerMessage();

    boolean invokeAnswerGroupMessage();

    boolean invokeSendMessage();

    boolean invokeSendGroupMessage();

    boolean invokeSendComment();

    boolean invokeSendTweet();

    boolean acceptInvite();
}
