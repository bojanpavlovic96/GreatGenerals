package root.communication.messages;

import java.util.Date;

public class ReadyForInitMsg extends Message {

	public ReadyForInitMsg(Date timestamp) {
		super(MessageType.ReadyForInitMsg, timestamp);
	}

}
