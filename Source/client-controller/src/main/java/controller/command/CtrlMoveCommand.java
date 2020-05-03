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

	private Point2D start_field;
	private Field base_field;

	private Point2D next_field;
	private Field second_field;

	public CtrlMoveCommand(Point2D first_position, Point2D second_position) {
		super("move-command");

		this.start_field = first_position;
		this.next_field = second_position;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.base_field = ((Controller) super.targetComponent).getModel().getField(this.start_field);
		this.second_field = ((Controller) super.targetComponent).getModel().getField(this.next_field);

	}

	@Override
	public void run() {

		Controller controller = (Controller) super.targetComponent;
		CommandQueue view_command_queue = controller.getConsumerQueue();

		this.base_field.getUnit().relocateTo(this.second_field);

		view_command_queue.enqueue(new UnselectFieldCommand(this.base_field));
		view_command_queue.enqueue(new UnselectFieldCommand(this.second_field));

		view_command_queue.enqueue(new ClearFieldCommand(this.base_field));
		view_command_queue.enqueue(new DrawFieldCommand(this.base_field));

		view_command_queue.enqueue(new ClearFieldCommand(this.second_field));
		view_command_queue.enqueue(new DrawFieldCommand(this.second_field));

		List<Field> unit_path = this.second_field.getUnit().getMoveType().getPath();

		unit_path.remove(0);

		// note: unit is now on second_field
		if (!unit_path.isEmpty()) {
			// continue moving

			this.second_field.getUnit().getMoveType().move();
			// trigger timer
		}

		if (this.base_field == controller.getSelectedField()) {

			controller.selectField(second_field);

		}

	}

	public Point2D getSecondPosition() {
		return next_field;
	}

	public void setSecondPosition(Point2D second_position) {
		this.next_field = second_position;
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
