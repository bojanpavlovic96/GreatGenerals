package view.command;

import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class UnselectFieldCommand extends Command {

	private Field model;

	private ViewField view_field;

	public UnselectFieldCommand(Field model) {
		super("unselect-field-view-command");

		this.model = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.target_component).convertToViewField(this.model);

	}

	public void run() {

		this.view_field.clearField(((View) super.target_component).getGraphicContext());
		this.view_field.drawOn(((View) super.target_component).getGraphicContext());

	}

}
