package view.command;

import root.command.Command;
import root.model.component.Field;
import root.view.field.ViewField;

public class UnselectUnitPath extends Command {

	private Field modelField;
	private ViewField viewField;

	public UnselectUnitPath() {
	}

	@Override
	public void run() {

	}

	@Override
	public Command getAntiCommand() {
		return new SelectUnitPathCommand(this.modelField);
	}

}
