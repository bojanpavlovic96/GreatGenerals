package controller.command;

import javafx.geometry.Point2D;
import model.Model;
import model.component.Field;

public class AttackCommand extends Command {

	private Point2D second_position;
	private Field second_field; // attacked field

	public AttackCommand(Point2D first_position, Point2D second_position) {

		super("attack-comand", first_position);
		this.second_position = second_position;

	}

	@Override
	public void setData_model(Model data_model) {
		super.setData_model(data_model);

		this.second_field = super.data_model.getField(this.second_position);

	}

	@Override
	public void run() {

		// if this command is executed, there is more place in the battle
		// or the battle just can be started

		// or the second_field is in range of the primary unit

		this.primary_field.moveToField(this.second_field);

		if (this.second_field.isInBattle()) {
			super.data_model.startBattle(this.second_field);
		}

		this.view_command_queue.enqueue(new view.command.AttackFieldCommand(primary_field, second_field));

	}

}
