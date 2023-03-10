package root.communication.messages;

public class IncomeTickMsg extends Message {

	public int amount;

	public IncomeTickMsg() {
		super(MessageType.IncomeTick);

	}

}
