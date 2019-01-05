package view.command;

import model.component.GameField;
import view.component.Hexagon;

public class ClearFieldCommand extends ViewCommand {

	private Hexagon hex;
	
	public ClearFieldCommand(GameField field) {
		
	}
	
	public ClearFieldCommand(Hexagon hex) {
		this.hex = hex;
	}

	public void run() {
		this.hex.clearHex(this.canvas.getGraphicsContext2D());
	}

}
