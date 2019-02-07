package view.command;

import model.component.field.Field;

public class DrawFieldCommand extends ViewCommand {

	public DrawFieldCommand(Field model) {
		super(model);
	}

	public void run() {
		this.field.drawOn(super.view.getMainCanvas());
	}

}
