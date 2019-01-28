package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class UnselectFieldCommand extends ViewCommand {

	public UnselectFieldCommand(HexagonField hex) {
		super(hex);

	}

	public UnselectFieldCommand(Field model) {
		super(model);

	}

	public void run() {

		this.hex.clearField(super.view.getMainCanvas().getGraphicsContext2D());
		this.hex.drawOn(super.view.getMainCanvas());

	}

}
