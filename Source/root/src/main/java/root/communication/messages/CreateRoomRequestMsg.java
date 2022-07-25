package root.communication.messages;

public class CreateRoomRequestMsg extends Message {

	// roomName and playerName are already in Message super-class 

	public String password;

	public CreateRoomRequestMsg(String roomName, String password, String playerName) {
		super(MessageType.CreateRoomRequest);

		super.roomName = roomName;
		super.player = playerName;

		this.password = password;
	}

}
