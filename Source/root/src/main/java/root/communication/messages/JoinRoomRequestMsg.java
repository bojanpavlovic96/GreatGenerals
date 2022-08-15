package root.communication.messages;

public class JoinRoomRequestMsg extends Message {

	public String password;

	public JoinRoomRequestMsg(String room, String password, String player) {
		super(MessageType.JoinRoomRequest);

		super.roomName = room;
		super.player = player;

		this.password = password;
	}

}
