package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class ClearFieldCommand extends ViewCommand {

	public ClearFieldCommand(Field model) {
		super(model);

	}

	public void run() {
		this.field.clearField(super.view.getMainCanvas().getGraphicsContext2D());
	}

}
