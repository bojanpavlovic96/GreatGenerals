package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class AbortMoveMsg extends Message {

	public Point2D unitPosition;

	public AbortMoveMsg(Date timestamp, String username, String roomName, Point2D unitPos) {
		super(MessageType.AbortMoveMessage, timestamp);

		super.setOrigin(username, roomName);

		this.unitPosition = unitPos;
	}

	
}