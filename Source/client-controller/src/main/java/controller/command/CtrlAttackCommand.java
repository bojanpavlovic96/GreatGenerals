package controller.command;

import root.Point2D;
import root.command.Command;

public class CtrlAttackCommand extends Command {

	public static final String name = "attack-command";

	public Point2D startFieldPosition;
	public Point2D endFieldPosition;

	public CtrlAttackCommand(Point2D startFieldPos, Point2D endFieldPos) {

		this.startFieldPosition = startFieldPos;
		this.endFieldPosition = endFieldPos;
	}

	@Override
	public void run() {
		

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
