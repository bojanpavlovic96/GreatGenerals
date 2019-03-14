package controller.command;

import controller.Controller;
import javafx.geometry.Point2D;
import model.Model;
import model.component.field.Field;
import view.command.CommandQueue;

public abstract class CtrlCommand implements Runnable {

	protected String command_name;

	protected Controller controller;

	// extracted from controller for quick access
	protected CommandQueue view_command_queue;
	protected Model model;

	protected Point2D base_position;
	protected Field base_field;

	// methods

	public CtrlCommand(String name, Point2D primary_position) {

		this.command_name = name;

		this.base_position = primary_position;

	}

	public CtrlCommand(Field primary_field) {
		this.setPrimaryField(primary_field);
	}

	public abstract void run();

	// getters and setters
	public Field getPrimaryField() {
		return base_field;
	}

	public void setPrimaryField(Field primary_field) {
		this.base_field = primary_field;
	}

	public String getName() {
		return command_name;
	}

	public void setName(String name) {
		this.command_name = name;
	}

	public CommandQueue getViewCommandQueue() {
		return view_command_queue;
	}

	public void setViewCommandQueue(CommandQueue view_command_queue) {
		this.view_command_queue = view_command_queue;
	}

	public void setController(Controller controller) {

		this.controller = controller;

		this.model = this.controller.getModel();
		this.view_command_queue = this.controller.getView().getCommandQueue();

		if (this.base_position != null) {
			this.base_field = this.model.getField(this.base_position);
		}

	}

}
