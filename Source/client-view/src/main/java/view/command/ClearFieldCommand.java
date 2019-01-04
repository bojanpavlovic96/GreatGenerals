package view.command;

import model.Field;
import view.component.Hexagon;

public class ClearFieldCommand extends ViewCommand {

	private Hexagon hex;
	
	public ClearFieldCommand(Field field) {
		
	}
	
	public ClearFieldCommand(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {
		this.hex.clearHex(this.canvas.getGraphicsContext2D());
	}

}
