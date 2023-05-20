package root.communication.messages;

import java.util.Date;

public class CreateRoomRequestMsg extends Message {

	public String password;

	public CreateRoomRequestMsg(Date timestamp, String roomName, String password, String playerName) {
		super(MessageType.CreateRoomRequest, timestamp);

		super.roomName = roomName;
		super.username = playerName;

		this.password = password;
	}

}
