package root.communication.messages;

import java.util.Date;

public class LeaveRoomRequestMsg extends Message {

	public LeaveRoomRequestMsg(Date timestamp,String roomName, String playerName) {
		super(MessageType.LeaveRoomRequest, timestamp);

		this.roomName = roomName;
		this.username = playerName;
	}

}
