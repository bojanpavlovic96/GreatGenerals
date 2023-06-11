package view.command;

import root.command.Command;
import root.model.component.Field;
import root.view.View;

public class DrawFieldCommand extends Command {

	private Field fieldModel;

	public DrawFieldCommand(Field model) {
		this.fieldModel = model;
	}

	public void run() {
		var viewField = ((View) targetComponent).convertToViewField(fieldModel);

		// This looks like a good spot to pass resource/asset manager
		// to the viewField (resource manager will load images or "assets")
		viewField.drawOn(((View) super.targetComponent).getMainGraphicContext());
	}

	@Override
	public Command getAntiCommand() {
		return new ClearFieldCommand(this.fieldModel);
	}

}
