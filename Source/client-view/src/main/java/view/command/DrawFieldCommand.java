package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class DrawFieldCommand extends ViewCommand {

	public static double default_hex_size = 20;
	public static double default_border_width = 5;

	public DrawFieldCommand(Field model) {
		super(model);
	}

	public DrawFieldCommand(Field model, double side_size, double border_width) {
		super(model);

		super.hex.setSideSize(side_size);
		super.hex.setBorderWidth(border_width);

	}

	public DrawFieldCommand(HexagonField hex) {
		super(hex);

	}

	public void run() {
		this.hex.drawOn(super.view.getMainCanvas());
	}

}
