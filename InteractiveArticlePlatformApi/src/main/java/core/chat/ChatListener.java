package core.chat;

@FunctionalInterface
public interface ChatListener {

    void messageReceiver(ChatMessage chatMessage);

}
