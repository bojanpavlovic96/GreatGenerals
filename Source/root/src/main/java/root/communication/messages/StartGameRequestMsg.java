package root.communication.messages;

import java.util.Date;

public class StartGameRequestMsg extends Message {

	public StartGameRequestMsg(Date timestamp, String roomName, String playerName) {
		super(MessageType.StartGameRequest, timestamp);

		super.roomName = roomName;
		super.username = playerName;
	}

}
