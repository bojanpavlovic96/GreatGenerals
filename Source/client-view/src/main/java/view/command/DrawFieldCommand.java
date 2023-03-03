package view.command;

import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;

public class DrawFieldCommand extends Command {

	private Field fieldModel;
	private ViewField viewField;

	public DrawFieldCommand(Field model) {

		this.fieldModel = model;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.viewField = ((View) super.targetComponent).convertToViewField(this.fieldModel);

	}

	public void run() {
		// This looks like a good spot to pass resource/asset manager
		// to the viewField (resource manager will load images or "assets")
		this.viewField.drawOn(((View) super.targetComponent).getMainGraphicContext());
	}

	@Override
	public Command getAntiCommand() {
		return new ClearFieldCommand(this.fieldModel);
	}

}
