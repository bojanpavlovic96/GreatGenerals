package root.communication.messages;

import root.Point2D;

public class AbortAttackMsg extends Message {

	public Point2D unitPosition;

	public AbortAttackMsg(String username, String roomName, Point2D unitPos) {
		super(MessageType.AbortAttackMessage);

		super.setOrigin(username, roomName);

		this.unitPosition = unitPos;
	}

}