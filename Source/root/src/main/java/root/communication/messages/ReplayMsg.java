package root.communication.messages;

import java.util.Date;
import java.util.List;

public class ReplayMsg extends Message {

	public Date startTimestamp;

	public List<Message> messages;

	public ReplayMsg(Date timestamp) {
		super(MessageType.ReplayMessage, timestamp);
	}

}
