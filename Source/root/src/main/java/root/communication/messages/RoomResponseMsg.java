package root.communication.messages;

import java.util.List;

import root.communication.PlayerDescription;

public class RoomResponseMsg extends Message {

	public RoomResponseType responseType;

	// initialize even in createRoom request and 
	// add only one element containing description of request sender
	public List<PlayerDescription> players;

	public RoomResponseMsg(RoomResponseType responseType, List<PlayerDescription> players) {
		super(MessageType.RoomResponse);

		this.responseType = responseType;
		this.players = players;
	}

}
