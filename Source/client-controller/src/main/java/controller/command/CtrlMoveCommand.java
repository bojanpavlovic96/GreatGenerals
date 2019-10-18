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

public class CtrlMoveCommand extends Command {

	private Point2D base_position;
	private Field base_field;

	private Point2D second_position;
	private Field second_field;

	public CtrlMoveCommand(Point2D first_position, Point2D second_position) {
		super("move-command");

		this.base_position = first_position;
		this.second_position = second_position;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.base_field = ((Controller) super.target_component).getModel().getField(this.base_position);
		this.second_field = ((Controller) super.target_component).getModel().getField(this.second_position);

	}

	@Override
	public void run() {

		// move unit
		this.base_field.getUnit().relocateTo(this.second_field);
		// !!! unit is now on second field

		// redraw both fields
		CommandQueue view_command_queue = ((Controller) super.target_component).getConsumerQueue();

		// clear both fields
		view_command_queue.enqueue(new ClearFieldCommand(this.base_field));
		view_command_queue.enqueue(new ClearFieldCommand(this.second_field));

		// then draw them again
		view_command_queue.enqueue(new DrawFieldCommand(this.base_field));
		view_command_queue.enqueue(new DrawFieldCommand(this.second_field));

		List<Field> unit_path = this.second_field.getUnit().getMoveType().getPath();

		unit_path.remove(0);

		// if path is not empty
		// !!! unit is now on second field
		if (!unit_path.isEmpty()) {
			// continue moving

			this.second_field.getUnit().getMoveType().move();
			// trigger timer

		}

	}

	public Point2D getSecondPosition() {
		return second_position;
	}

	public void setSecondPosition(Point2D second_position) {
		this.second_position = second_position;
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
