package view.command;

import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class DrawFieldCommand extends Command {

	private Field model;
	private ViewField view_Field;

	public DrawFieldCommand(Field model) {
		super("draw-field-view-command");

		this.model = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_Field = ((View) super.target_component).convertToViewField(this.model);

	}

	public void run() {
		this.view_Field.drawOn(((View) super.target_component).getMainGraphicContext());
	}

}
