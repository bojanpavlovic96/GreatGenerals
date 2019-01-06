package view.command;

import model.component.Field;
import view.component.Hexagon;

public class UnselectFieldCommand extends ViewCommand {

	public UnselectFieldCommand(Hexagon hex) {
		super(hex);

	}

	public UnselectFieldCommand(Field model) {
		super(model);

	}

	public void run() {

		this.hex.clearHex(super.view.getMainCanvas().getGraphicsContext2D());
		this.hex.drawOn(super.view.getMainCanvas());

	}

}
