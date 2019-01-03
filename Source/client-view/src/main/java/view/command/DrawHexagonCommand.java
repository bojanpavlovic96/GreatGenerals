package view.command;

import model.Field;
import view.component.Hexagon;

public class DrawHexagonCommand extends ViewCommand {

	private static double default_hex_size = 50;

	private Hexagon hex;

	public DrawHexagonCommand(Field hex) {
		this.hex = new Hexagon(hex, DrawHexagonCommand.default_hex_size);
	}

	public DrawHexagonCommand(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {
		this.hex.drawOn(this.canvas);
	}

}
