package view.command;

import model.component.Field;
import view.component.Hexagon;

public class RedrawFieldCommand extends ViewCommand {

	public RedrawFieldCommand(Field model) {
		super(model);
	}

	public RedrawFieldCommand(Field model, double side_size, double border_width) {
		super(model);

		super.hex.setBorderWidth(border_width);
		super.hex.setSideSize(side_size);

	}

	public RedrawFieldCommand(Hexagon hex) {
		super(hex);
	}

	public void run() {

		this.hex.clearHex(super.view.getMainCanvas().getGraphicsContext2D());
		this.hex.drawOn(super.view.getMainCanvas());

	}

}
