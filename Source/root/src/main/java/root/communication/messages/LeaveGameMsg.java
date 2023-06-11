package root.communication.messages;

import java.util.Date;

public class LeaveGameMsg extends Message {

	public LeaveGameMsg(Date timestamp) {
		super(MessageType.LeaveGame, timestamp);

	}

}
