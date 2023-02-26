package root.communication.messages;

import root.Point2D;

public class DefendMsg extends Message {

	public String defendType;

	public Point2D startFieldPos;
	public Point2D endFieldPos;

	public DefendMsg(String defendType, Point2D startFieldPos, Point2D endFieldPos) {
		super(MessageType.DefendMessage);

		this.defendType = defendType;
		this.startFieldPos = startFieldPos;
		this.endFieldPos = endFieldPos;
	}
}
