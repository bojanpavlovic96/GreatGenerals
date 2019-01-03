package fields.command;

import fields.draw.Hexagon;

public class ClearHex extends ViewCommand {

	private Hexagon hex;

	public ClearHex(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {
		this.hex.clearHex(this.canvas.getGraphicsContext2D());
	}

}
