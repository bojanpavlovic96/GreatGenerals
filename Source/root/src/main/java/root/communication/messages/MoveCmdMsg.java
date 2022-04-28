package root.communication.messages;

import javafx.geometry.Point2D;

public class MoveCmdMsg extends Message {

	public static final String name = "move-comd-msg";

	// TODO please create custom point2D class ... 
	public Point2D startFieldPos;
	public Point2D endFieldPos;

	public MoveCmdMsg() {
		super(MoveCmdMsg.name);
	}

}
