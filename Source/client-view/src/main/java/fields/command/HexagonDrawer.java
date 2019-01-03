package fields.command;

import fields.draw.Hexagon;

public class HexagonDrawer extends ViewCommand {

	private Hexagon hex;

	public HexagonDrawer(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {
		this.hex.drawOn(this.canvas);
	}

}
