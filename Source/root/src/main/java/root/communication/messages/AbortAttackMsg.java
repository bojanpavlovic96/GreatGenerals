package root.communication.messages;

import root.Point2D;

public class AbortAttackMsg extends Message {

	public Point2D unitPosition;

	public AbortAttackMsg(Point2D unitPos) {
		super(MessageType.AbortAttackMessage);

		this.unitPosition = unitPos;
	}

}