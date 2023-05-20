package root.communication.messages;

import java.util.Date;

public class ReadyForReplayMsg extends Message {

	public ReadyForReplayMsg(Date timestamp) {
		super(MessageType.ReadyForReplayMsg, timestamp);
	}

}
