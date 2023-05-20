package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class AbortAttackMsg extends Message {

	public Point2D unitPosition;

	public AbortAttackMsg(Date timestamp, Point2D unitPos) {
		super(MessageType.AbortAttackMessage, timestamp);

		this.unitPosition = unitPos;
	}

}