package controller.command;

import root.Point2D;
import root.command.Command;
import root.controller.Controller;

public class CtrlAbortAttackCommand extends Command {

	private Point2D sourcePosition;

	public CtrlAbortAttackCommand(Point2D sourcePosition) {
		this.sourcePosition = sourcePosition;
	}

	@Override
	public void run() {
		System.out.println("Executing abortAttackCommand ... ");
		var controller = (Controller) targetComponent;
		var sourceField = controller.getModel().getField(sourcePosition);

		if (sourceField.getUnit() == null) {
			System.out.println("Failed to abort the attack, unit doesn't exist ... ");
			return;
		}

		var iAmAttacking = controller.isOwner(sourceField.getUnit().getOwner().getUsername());

		if (!iAmAttacking) {
			System.out.println("Defense aborted ...");
			if (sourceField.getUnit().isAttacking()) {
				var defendedField = sourceField.getUnit().getActiveAttack().getTarget();
				defendedField.getUnit().deactivateDefense();
			}
		}

		System.out.println("Attack aborted ...");
		sourceField.getUnit().deactivateAttack();
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
