package controller.command;

import root.Point2D;
import root.command.Command;

public class CtrlRecalculatePathCommand extends Command {

	protected Point2D sourcePosition;

	public CtrlRecalculatePathCommand(Point2D sourcePos) {
		this.sourcePosition = sourcePos;
	}

	@Override
	public void run() {
		System.out.println("RECALCULATE PATH COMMAND NOT IMPLEMENTED ... ");
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
