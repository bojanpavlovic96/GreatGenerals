package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class ClearFieldCommand extends ViewCommand {

	public ClearFieldCommand(Field model) {
		super(model);

	}

	public ClearFieldCommand(HexagonField hex) {
		super(hex);

	}

	public void run() {
		this.hex.clearField(super.view.getMainCanvas().getGraphicsContext2D());
	}

}
