package controller.command;

import root.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.controller.Controller;
import root.model.component.Field;

public class CtrlAttackCommand extends Command {

	public Point2D startFieldPosition;
	public Point2D endFieldPosition;

	public Field startField;
	public Field endField;

	public CtrlAttackCommand(Point2D startFieldPos, Point2D endFieldPos) {

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
		var viewQueue = controller.getConsumerQueue();

		// endField.setUnit(startField.getUnit());
		// startField.setUnit(null);

	}

	@Override
	public Command getAntiCommand() {
		return null; // abort attack command 
	}

}
