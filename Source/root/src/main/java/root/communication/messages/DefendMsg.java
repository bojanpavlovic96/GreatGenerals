package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class DefendMsg extends Message {

	public String defendType;

	public Point2D startFieldPos;
	public Point2D endFieldPos;

	public DefendMsg(Date timestamp,String defendType, Point2D startFieldPos, Point2D endFieldPos) {
		super(MessageType.DefendMessage,timestamp);

		this.defendType = defendType;
		this.startFieldPos = startFieldPos;
		this.endFieldPos = endFieldPos;
	}
}
