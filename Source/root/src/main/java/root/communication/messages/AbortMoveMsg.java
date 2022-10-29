package root.communication.messages;

import root.Point2D;

public class AbortMoveMsg extends Message {

	public Point2D unitPosition;

	public AbortMoveMsg(String username, String roomName, Point2D unitPos) {
		super(MessageType.AbortMoveMessage);

		super.setOrigin(username, roomName);

		this.unitPosition = unitPos;
	}

}
