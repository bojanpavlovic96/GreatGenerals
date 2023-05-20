package root.communication.messages;

import java.util.Date;

public class GameDoneMsg extends Message {

	public int bonusAmount;

	public GameDoneMsg(Date timestamp) {
		super(MessageType.GameDone, timestamp);
	}

}
