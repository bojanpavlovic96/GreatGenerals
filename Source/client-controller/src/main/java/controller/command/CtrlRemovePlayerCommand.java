package controller.command;

import java.util.List;
import java.util.stream.Collectors;

import root.command.Command;
import root.controller.Controller;
import root.model.component.Field;
import view.command.DrawFieldCommand;

public class CtrlRemovePlayerCommand extends Command {

	private String whoLeft;

	public CtrlRemovePlayerCommand(String whoLeft) {
		this.whoLeft = whoLeft;
	}

	@Override
	public void run() {
		var controller = (Controller) targetComponent;

		if (controller.isOwner(whoLeft)) {
			// my guy left 

			((root.ActiveComponent) controller).shutdown();

		} else {
			// somebody else left 

			List<Field> leftUnitFields = controller.getModel()
					.getActiveUnits().stream()
					.filter((u) -> u.getOwner().getUsername().equals(whoLeft))
					.map((u) -> u.getField())
					.collect(Collectors.toList());

			var myAttackingUnits = controller.getModel()
					.getActiveUnits().stream()
					.filter(u -> controller.isOwner(u.getOwner().getUsername()))
					.filter(u -> u.isAttacking())
					.filter(u -> leftUnitFields.contains(u.getActiveAttack().getTarget()))
					.collect(Collectors.toList());

			for (var unit : myAttackingUnits) {
				unit.deactivate();
			}

			for (var field : leftUnitFields) {
				field.getUnit().deactivate();
				controller.getModel().removeUnit(field.getUnit());
				field.setUnit(null);

				var redrawCmd = new DrawFieldCommand(field);
				controller.getView().getCommandQueue().enqueue(redrawCmd);
			}

			controller.getModel().removePlayer(whoLeft);
		}
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
