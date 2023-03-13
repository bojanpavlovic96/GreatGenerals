package root.communication.messages;

public class GameDoneMsg extends Message {

	public int bonusAmount;

	public GameDoneMsg() {
		super(MessageType.GameDone);
	}

}
