package controller.command;

import java.util.List;

import javafx.geometry.Point2D;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.command.CommandQueue;
import root.controller.Controller;
import root.model.component.Field;
import view.command.ClearFieldCommand;
import view.command.DrawFieldCommand;
import view.command.UnselectFieldCommand;

public class CtrlMoveCommand extends Command {

	private Point2D startField;
	private Field baseField;

	private Point2D nextField;
	private Field secondField;

	public CtrlMoveCommand(Point2D first_position, Point2D second_position) {
		super("move-command");

		this.startField = first_position;
		this.nextField = second_position;
	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.baseField = ((Controller) super.targetComponent).getModel().getField(this.startField);
		this.secondField = ((Controller) super.targetComponent).getModel().getField(this.nextField);
	}

	@Override
	public void run() {

		Controller controller = (Controller) super.targetComponent;
		CommandQueue viewCommandQueue = controller.getConsumerQueue();

		this.baseField.getUnit().relocateTo(this.secondField);

		viewCommandQueue.enqueue(new UnselectFieldCommand(this.baseField));
		viewCommandQueue.enqueue(new UnselectFieldCommand(this.secondField));

		viewCommandQueue.enqueue(new ClearFieldCommand(this.baseField));
		viewCommandQueue.enqueue(new DrawFieldCommand(this.baseField));

		viewCommandQueue.enqueue(new ClearFieldCommand(this.secondField));
		viewCommandQueue.enqueue(new DrawFieldCommand(this.secondField));

		List<Field> unitPath = this.secondField.getUnit().getMoveType().getPath();

		unitPath.remove(0);

		// note: unit is now on second_field
		if (!unitPath.isEmpty()) {
			// continue moving

			this.secondField.getUnit().getMoveType().move();
			// trigger timer
		}

		if (this.baseField == controller.getSelectedField()) {
			controller.selectField(secondField);
		}

	}

	public Point2D getSecondPosition() {
		return nextField;
	}

	public void setSecondPosition(Point2D second_position) {
		this.nextField = second_position;
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
