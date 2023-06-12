package root.communication.messages;

import java.util.Date;

public class RemovePlayerMsg extends Message {

	public String whoLeft;

	public RemovePlayerMsg(Date timestamp, String whoLeft) {
		super(MessageType.RemovePlayer, timestamp);
		this.whoLeft = whoLeft;
	}

}
