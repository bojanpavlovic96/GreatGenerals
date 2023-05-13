package root.communication.messages;

import java.util.List;

public class ReplayMessage extends Message {

	public List<Message> messages;

	public ReplayMessage() {
		super(MessageType.ReplayMessage);
	}

}
