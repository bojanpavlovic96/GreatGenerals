package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class RecalculatePathMsg extends Message {

	public Point2D unitPosition;

	public RecalculatePathMsg(Date timestamp,String username, String roomName, Point2D unitPos) {
		super(MessageType.RecalculatePathMessage, timestamp);

		super.setOrigin(username, roomName);

		this.unitPosition = unitPos;
	}

}
