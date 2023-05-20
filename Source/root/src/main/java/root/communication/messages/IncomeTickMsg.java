package root.communication.messages;

import java.util.Date;

public class IncomeTickMsg extends Message {

	public int amount;

	public IncomeTickMsg(Date timestamp) {
		super(MessageType.IncomeTick, timestamp);

	}

}
