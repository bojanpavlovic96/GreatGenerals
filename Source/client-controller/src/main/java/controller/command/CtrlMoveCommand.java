package controller.command;

import controller.Controller;
import javafx.geometry.Point2D;
import model.component.field.Field;
import view.command.RedrawFieldCommand;

public class CtrlMoveCommand extends CtrlCommand {

	private Point2D second_position;
	private Field second_field;

	public CtrlMoveCommand(Point2D first_position, Point2D second_position) {
		super("move-command", first_position);
		// sets command name for database storing
		// sets primary position

		this.setSecondPosition(second_position);

	}

	@Override
	public void run() {

		// move unit
		this.base_field.getUnit().moveTo(this.second_field);

		// redraw both fields
		this.view_command_queue.enqueue(new RedrawFieldCommand(this.base_field));
		this.view_command_queue.enqueue(new RedrawFieldCommand(this.second_field));

		this.second_field.getUnit().getMoveType().getPath().remove(0);

		// if path is not empty ...
		if (!this.second_field.getUnit().getMoveType().getPath().isEmpty()) {
			// continue moving

			this.second_field.getUnit().getMoveType().move();
			// triger timer

		}

	}

	public Point2D getSecondPosition() {
		return second_position;
	}

	public void setSecondPosition(Point2D second_position) {
		this.second_position = second_position;
	}

	@Override
	public void setController(Controller controller) {
		super.setController(controller);

		this.second_field = this.model.getField(this.second_position);

	}
}
