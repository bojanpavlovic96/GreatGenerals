package controller.command;

import root.Point2D;
import root.command.Command;

public class CtrlAbortMoveCommand extends Command {

	protected Point2D sourceField;

	public CtrlAbortMoveCommand(Point2D sourceField) {
		this.sourceField = sourceField;
	}

	@Override
	public void run() {
		System.out.println("ABORT MOVE COMMAND NOT IMPLEMENTED ... ");
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
