package root.communication.messages;

import java.util.Date;

public class JoinRoomRequestMsg extends Message {

	public String password;

	public JoinRoomRequestMsg(Date timestamp,String room, String password, String player) {
		super(MessageType.JoinRoomRequest,timestamp);

		super.roomName = room;
		super.username = player;

		this.password = password;
	}

}
