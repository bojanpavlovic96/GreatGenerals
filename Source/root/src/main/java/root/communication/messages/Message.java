package root.communication.messages;

public class Message {

	public MessageType type;

	public String username;
	public String roomName;

	public Message(MessageType type) {
		this.type = type;
	}

	public void setOrigin(String player, String roomName) {
		this.username = player;
		this.roomName = roomName;
	}

}
