package root.communication.messages;

import root.Point2D;

public class AbortDefenseMsg extends Message {

	public Point2D unitPosition;

	public AbortDefenseMsg() {
		super(MessageType.AbortDefenseMessage);
	}

}
