package view.command;

import root.command.Command;
import root.model.component.Field;
import root.view.field.ViewField;

public class UnselectUnitPath extends Command {

	public static final String commandName = "unselect-unit-path-command";

	private Field modelField;
	private ViewField viewField;

	public UnselectUnitPath() {
		super(UnselectUnitPath.commandName);
	}

	@Override
	public void run() {

	}

	@Override
	public Command getAntiCommand() {
		return new SelectUnitPathCommand(this.modelField);
	}

}
