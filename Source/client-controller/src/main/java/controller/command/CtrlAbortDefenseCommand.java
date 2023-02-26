package controller.command;

import root.Point2D;
import root.command.Command;
import root.controller.Controller;

public class CtrlAbortDefenseCommand extends Command {

	private Point2D sourcePosition;

	public CtrlAbortDefenseCommand(Point2D sourcePosition) {
		this.sourcePosition = sourcePosition;
	}

	@Override
	public void run() {

		var controller = (Controller) getTargetComponent();
		var sourceField = controller.getModel().getField(sourcePosition);

		if (sourceField.getUnit() == null) {
			System.out.println("Failed to abort the defense, unit doesn't exist ... ");
			return;
		}

		if(sourceField.getUnit().isDefending()){
			sourceField.getUnit().deactivateDefense();
		}

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
