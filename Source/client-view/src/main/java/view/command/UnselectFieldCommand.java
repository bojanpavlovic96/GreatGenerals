package view.command;

import model.component.Field;
import view.component.Hexagon;

public class UnselectFieldCommand extends ViewCommand {

	private Hexagon hex;

	public UnselectFieldCommand(Hexagon hex) {
		super();

		this.hex = hex;
	}

	public UnselectFieldCommand(Field model) {

		super();
		
		Hexagon hex = new Hexagon(model);
		hex.setBorder_width(DrawFieldCommand.default_border_width);
		this.hex = hex;

	}

	public void run() {

		this.hex.clearHex(this.canvas.getGraphicsContext2D());
		this.hex.drawOn(this.canvas);

	}

}
