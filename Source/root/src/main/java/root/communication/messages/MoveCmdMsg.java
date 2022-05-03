package root.communication.messages;

import root.Point2D;

public class MoveCmdMsg extends Message {

	public static final String name = "move-cmd-msg";

	public Point2D startFieldPos;
	public Point2D endFieldPos;

	public MoveCmdMsg(Point2D startField, Point2D endField) {
		super(MoveCmdMsg.name);

		this.startFieldPos = startField;
		this.endFieldPos = endField;
	}

}
