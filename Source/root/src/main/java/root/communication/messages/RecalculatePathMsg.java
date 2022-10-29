package root.communication.messages;

import root.Point2D;

public class RecalculatePathMsg extends Message {

	public Point2D unitPosition;

	public RecalculatePathMsg(String username, String roomName, Point2D unitPos) {
		super(MessageType.RecalculatePathMessage);

		super.setOrigin(username, roomName);

		this.unitPosition = unitPos;
	}

}
