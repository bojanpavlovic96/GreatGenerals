package root.communication.messages;

public class CreateRoomRequestMsg extends Message {

	public String password;

	public CreateRoomRequestMsg(String roomName, String password, String playerName) {
		super(MessageType.CreateRoomRequest);

		super.roomName = roomName;
		super.username = playerName;

		this.password = password;
	}

}
