package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class RedrawFieldCommand extends ViewCommand {

	public RedrawFieldCommand(Field model) {
		super(model);
	}

	public void run() {

		this.field.clearField(super.view.getMainCanvas().getGraphicsContext2D());
		this.field.drawOn(super.view.getMainCanvas());

	}

}
