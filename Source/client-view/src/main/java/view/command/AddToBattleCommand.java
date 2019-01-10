package view.command;

import model.component.field.Field;
import view.component.Hexagon;

public class AddToBattleCommand extends ViewCommand {

	private Hexagon adding_hex;
	private Hexagon battle_hex;

	public AddToBattleCommand(Field adding_field, Field battle_field) {

		this.adding_hex = new Hexagon(adding_field, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);

		this.battle_hex = new Hexagon(battle_field, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);

	}

	public void run() {
		
		
		
	}

}
