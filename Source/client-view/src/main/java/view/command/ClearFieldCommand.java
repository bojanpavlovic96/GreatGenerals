package view.command;

import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class ClearFieldCommand extends Command {

	private Field model;
	private ViewField viewField;

	public ClearFieldCommand(Field model) {
		this.model = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewField = ((View) super.targetComponent).convertToViewField(this.model);

	}

	public void run() {
		this.viewField.clearField(((View) super.targetComponent).getMainGraphicContext());
	}

	@Override
	public Command getAntiCommand() {
		return new DrawFieldCommand(this.model);
	}

}
