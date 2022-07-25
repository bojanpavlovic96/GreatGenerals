package root.communication.messages;

public class Message {

	public MessageType type;

	public String roomName;
	public String player;

	public Message(MessageType type) {
		this.type = type;
	}

	public void setOrigin(String player, String roomName) {
		this.player = player;
		this.roomName = roomName;
	}

}
