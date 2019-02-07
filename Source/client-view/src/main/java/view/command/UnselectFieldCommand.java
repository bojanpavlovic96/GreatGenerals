package view.command;

import model.component.field.Field;
import view.component.HexagonField;
import view.component.ViewField;

public class UnselectFieldCommand extends ViewCommand {

	public UnselectFieldCommand(Field model) {
		super(model);
	}

	public void run() {

		this.field.clearField(super.view.getMainCanvas().getGraphicsContext2D());
		this.field.drawOn(super.view.getMainCanvas());

	}

}
