package root.communication.messages;

public class LeaveRoomRequestMsg extends Message {

	public LeaveRoomRequestMsg(String roomName, String playerName) {
		super(MessageType.LeaveRoomRequest);

		this.roomName = roomName;
		this.player = playerName;
	}

}
