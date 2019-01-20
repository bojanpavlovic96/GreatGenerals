package controller.command;

import javafx.geometry.Point2D;
import model.Model;
import model.component.field.Field;
import view.command.CommandQueue;

public abstract class CtrlCommand implements Runnable {

	protected String command_name;

	protected Point2D primary_position;
	protected Field primary_field;

	protected Model data_model;
	protected CommandQueue view_command_queue;

	public CtrlCommand(String name, Point2D primary_position) {

		this.command_name = name;

		this.primary_position = primary_position;

	}

	public CtrlCommand(Field primary_field) {
		this.setPrimary_field(primary_field);
	}

	public abstract void run();

	// getters and setters
	public Field getPrimary_field() {
		return primary_field;
	}

	public void setPrimary_field(Field primary_field) {
		this.primary_field = primary_field;
	}

	public String getName() {
		return command_name;
	}

	public void setName(String name) {
		this.command_name = name;
	}

	public CommandQueue getView_command_queue() {
		return view_command_queue;
	}

	public void setView_command_queue(CommandQueue view_command_queue) {
		this.view_command_queue = view_command_queue;
	}

	public Model getData_model() {
		return data_model;
	}

	public void setData_model(Model data_model) {
		this.data_model = data_model;

		// get field based on primary_position
		this.primary_field = this.data_model.getField(this.primary_position);

	}

}
