package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class MoveMsg extends Message {

	public Point2D startFieldPos;
	public Point2D endFieldPos;

	public MoveMsg(Date timestamp,Point2D startField, Point2D endField) {
		super(MessageType.MoveMessage,timestamp);

		this.startFieldPos = startField;
		this.endFieldPos = endField;
	}

}
