package view.command;

import model.component.Field;
import view.component.Hexagon;

public class RedrawFieldCommand extends ViewCommand {

	private Hexagon hex;

	public RedrawFieldCommand(Field model) {
		this.hex = new Hexagon(model, DrawFieldCommand.default_hex_size, DrawFieldCommand.default_border_width);
	}

	public RedrawFieldCommand(Field model, double side_size, double border_width) {
		this.hex = new Hexagon(model, side_size, border_width);
	}

	public RedrawFieldCommand(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {

		this.hex.clearHex(this.canvas.getGraphicsContext2D());
		this.hex.drawOn(this.canvas);

	}

}
