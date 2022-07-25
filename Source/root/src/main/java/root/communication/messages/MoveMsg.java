package root.communication.messages;

import root.Point2D;

public class MoveMsg extends Message {

	public Point2D startFieldPos;
	public Point2D endFieldPos;

	public MoveMsg(Point2D startField, Point2D endField) {
		super(MessageType.MoveMessage);

		this.startFieldPos = startField;
		this.endFieldPos = endField;
	}

}
