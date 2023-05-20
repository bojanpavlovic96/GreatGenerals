package root.communication.messages;

import java.util.Date;

public class Message {

	public MessageType type;

	public String username;
	public String roomName;

	public Date timestamp;

	public Message(MessageType type, Date timestamp) {
		this.type = type;
		this.timestamp = timestamp;
	}

	public void setOrigin(String username, String roomName) {
		this.username = username;
		this.roomName = roomName;
	}

}
