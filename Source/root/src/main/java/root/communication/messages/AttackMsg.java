package root.communication.messages;

import java.util.Date;

import root.Point2D;

public class AttackMsg extends Message {

	public String attackType;

	public Point2D startFieldPos;

	public Point2D endFieldPos;

	public AttackMsg(Date timestamp,String attackType, Point2D startFieldPos, Point2D endFieldPos) {
		super(MessageType.AttackMessage, timestamp);

		this.attackType = attackType;
		this.startFieldPos = startFieldPos;
		this.endFieldPos = endFieldPos;
	}

}
