package root.communication.messages;

public class Message {

	public String name;

	public String roomName;
	public String player;

	public Message(String name) {
		this.name = name;
	}

	public void setOrigin(String player, String roomName) {
		this.player = player;
		this.roomName = roomName;
	}

}
