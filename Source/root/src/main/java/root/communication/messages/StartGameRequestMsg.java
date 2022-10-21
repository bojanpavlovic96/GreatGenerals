package root.communication.messages;

public class StartGameRequestMsg extends Message {

	public StartGameRequestMsg(String roomName, String playerName) {
		super(MessageType.StartGameRequest);

		super.roomName = roomName;
		super.username = playerName;
	}

}
