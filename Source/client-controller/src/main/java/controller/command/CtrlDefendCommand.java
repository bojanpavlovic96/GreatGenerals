package controller.command;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import view.command.ClearFieldCommand;
import view.command.DrawFieldCommand;
import view.command.ShowFieldDescription;
import root.controller.Controller;

public class CtrlDefendCommand extends Command {
	public String defenseType;

	public Point2D startFieldPosition;
	public Point2D endFieldPosition;

	public Field startField;
	public Field endField;

	public CtrlDefendCommand(String defenseType,
			Point2D startFieldPosition,
			Point2D endFieldPosition) {

		this.defenseType = defenseType;
		this.startFieldPosition = startFieldPosition;
		this.endFieldPosition = endFieldPosition;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		this.targetComponent = target;

		var model = ((Controller) target).getModel();

		this.startField = model.getField(this.startFieldPosition);
		this.endField = model.getField(this.endFieldPosition);
	}

	@Override
	public void run() {
		var controller = (Controller) targetComponent;

		// handle when before receiving thi 
		var defense = startField.getUnit().getDefense();
		endField.getUnit().defendFromWith(defense);

		System.out.println("defendedWith: " + defense.defenseDmg + "h: " + endField.getUnit().getHealth());

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

			var dist = controller.getModel().distance(startField, endField);
			var inDefenseRange = dist <= startField.getUnit().getDefense().defenseRange;

			if (!iAmAttacking() && inDefenseRange) {
				startField.getUnit().getDefense().defend();
			}
		}

		var descMenuVisible = controller.getView().getDescriptionMenu().isVisible();
		var describingStartField = controller.getFocusedField() == startField;
		var describingEndField = controller.getFocusedField() == endField;

		if (descMenuVisible && (describingStartField || describingEndField)) {
			controller
					.getView()
					.getCommandQueue()
					.enqueue(new ShowFieldDescription(controller.getFocusedField()));
		}

	}

	private boolean iAmAttacking() {
		return !((Controller) targetComponent).isOwner(endField.getPlayer().getUsername());
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}
}
