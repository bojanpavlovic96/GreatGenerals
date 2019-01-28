package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class AddToBattleCommand extends ViewCommand {

	private HexagonField adding_hex;
	private HexagonField battle_hex;

	public AddToBattleCommand(Field adding_field, Field battle_field) {

		this.adding_hex = new HexagonField(adding_field, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);

		this.battle_hex = new HexagonField(battle_field, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);

	}

	public void run() {
		
		
		
	}

}
