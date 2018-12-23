package chat;

public interface ChatController {

	void sendTo(String user_id);

	Message receiveFrom(String user_id);

}
