package view.command;

import model.Field;
import view.component.Hexagon;

public class DrawFieldCommand extends ViewCommand {

	public static double default_hex_size = 20;
	public static double default_border_width = 5;

	private Hexagon hex;

	public DrawFieldCommand(Field model) {
		this.hex = new Hexagon(model, DrawFieldCommand.default_hex_size, DrawFieldCommand.default_border_width);
	}

	public DrawFieldCommand(Field model, double side_size, double border_width) {
		this.hex = new Hexagon(model, side_size, border_width);
	}

	public DrawFieldCommand(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {
		this.hex.drawOn(this.canvas);
	}

}
