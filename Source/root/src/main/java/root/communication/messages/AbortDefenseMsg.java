package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class AbortDefenseMsg extends Message {

	public Point2D unitPosition;

	public AbortDefenseMsg(Date timestamp) {
		super(MessageType.AbortDefenseMessage, timestamp);
	}

}
