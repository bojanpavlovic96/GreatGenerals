package controller.command;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.controller.Controller;
import root.model.component.Field;
import view.command.ClearFieldCommand;
import view.command.DrawFieldCommand;

public class CtrlAttackCommand extends Command {

	public String attackType;

	public Point2D startFieldPosition;
	public Point2D endFieldPosition;

	public Field startField;
	public Field endField;

	public CtrlAttackCommand(String attackType, Point2D startFieldPos, Point2D endFieldPos) {

		this.attackType = attackType;

		this.startFieldPosition = startFieldPos;
		this.endFieldPosition = endFieldPos;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.startField = ((Controller) super.targetComponent)
				.getModel()
				.getField(this.startFieldPosition);
		this.endField = ((Controller) super.targetComponent)
				.getModel()
				.getField(this.endFieldPosition);
	}

	@Override
	public void run() {
		var controller = (Controller) super.getTargetComponent();

		var attack = startField.getUnit().getAttack(attackType);
		endField.getUnit().attackWith(attack);

		if (!endField.getUnit().isAlive()) {
			controller.getModel().removeUnit(endField.getUnit());

			endField.getUnit().deactivate();
			startField.getUnit().deactivate();

			endField.setUnit(null);
		}

		if (endField.getUnit() == null) {
			var viewQueue = controller.getView().getCommandQueue();
			viewQueue.enqueue(new ClearFieldCommand(endField));
			viewQueue.enqueue(new DrawFieldCommand(endField));
		} else {
			if (controller.isOwner(endField.getPlayer().getUsername())) {
				attack.attack();
			}
		}

	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
