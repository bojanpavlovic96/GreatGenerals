package root.communication.messages;

public class Message {

	public MessageType type;

	public String username;
	public String roomName;

	public Message(MessageType type) {
		this.type = type;
	}

	public void setOrigin(String username, String roomName) {
		this.username = username;
		this.roomName = roomName;
	}

}
