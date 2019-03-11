package controller.command;

import javafx.geometry.Point2D;
import model.Model;
import model.component.field.Field;
import view.command.RedrawFieldCommand;

public class CtrlMoveCommand extends CtrlCommand {

	private Point2D second_position;
	private Field second_field;

	public CtrlMoveCommand(Point2D first_position, Point2D second_position) {

		super("move-command", first_position);
		// sets command name for database storing
		// sets primary position

		this.second_position = second_position;

	}

	public CtrlMoveCommand(Field primary_field, Field second_field) {
		super(primary_field);

		this.second_field = second_field;

		super.setName("move-command"); // identify command for database storing
	}

	
	
	@Override
	public void run() {

		// this.primary_field.moveToField(this.second_field);
		// moves all necessary data to another field

		this.view_command_queue.enqueue(new RedrawFieldCommand(this.base_field));
		this.view_command_queue.enqueue(new RedrawFieldCommand(this.second_field));
		// redraw both fields

	}

}
