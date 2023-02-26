package controller.command;

import root.Point2D;
import root.command.Command;
import root.controller.Controller;

public class CtrlRecalculatePathCommand extends Command {

	protected Point2D sourcePos;

	public CtrlRecalculatePathCommand(Point2D sourcePos) {
		this.sourcePos = sourcePos;
	}

	@Override
	public void run() {

		var controller = (Controller) targetComponent;

		var sourceField = controller.getModel().getField(sourcePos);

		var oldPath = sourceField.getUnit().getMove().getPath();
		var dest = oldPath.get(oldPath.size() - 1);

		var newPath = sourceField
				.getUnit()
				.getMove()
				.calculatePath(controller.getModel(), sourceField, dest);

		if (newPath == null || newPath.size() == 0) {
			System.out.println("Path recalculation not possible ... ");
			return;
		}

		newPath.add(0, sourceField);

		sourceField.getUnit().getMove().move();
	}

	@Override
	public Command getAntiCommand() {
		// I guess stopMoving would be the most appropriate ... ? 
		return null;
	}

}
