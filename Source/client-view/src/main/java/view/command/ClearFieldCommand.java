package view.command;

import model.component.field.Field;
import view.component.Hexagon;

public class ClearFieldCommand extends ViewCommand {

	public ClearFieldCommand(Field model) {
		super(model);

	}

	public ClearFieldCommand(Hexagon hex) {
		super(hex);

	}

	public void run() {
		this.hex.clearHex(super.view.getMainCanvas().getGraphicsContext2D());
	}

}
