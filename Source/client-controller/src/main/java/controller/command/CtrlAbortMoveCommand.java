package controller.command;

import root.Point2D;
import root.command.Command;
import root.controller.Controller;

public class CtrlAbortMoveCommand extends Command {

	protected Point2D sourcePosition;

	public CtrlAbortMoveCommand(Point2D sourcePosition) {
		this.sourcePosition = sourcePosition;
	}

	@Override
	public void run() {
		var controller = (Controller) targetComponent;
		var field = controller.getModel().getField(sourcePosition);

		if (field.getUnit() != null && field.getUnit().getMove() != null) {
			field.getUnit().getMove().stopMoving();
		}
		// TODO do I clear the path as well ... ? 
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
