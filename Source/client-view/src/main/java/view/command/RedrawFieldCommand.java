package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class RedrawFieldCommand extends ViewCommand {

	public RedrawFieldCommand(Field model) {
		super(model);
	}

	public RedrawFieldCommand(Field model, double side_size, double border_width) {
		super(model);

		super.hex.setBorderWidth(border_width);
		super.hex.setSideSize(side_size);

	}

	public RedrawFieldCommand(HexagonField hex) {
		super(hex);
	}

	public void run() {

		this.hex.clearField(super.view.getMainCanvas().getGraphicsContext2D());
		this.hex.drawOn(super.view.getMainCanvas());

	}

}
