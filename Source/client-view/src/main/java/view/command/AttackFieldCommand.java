package view.command;

import model.component.field.Field;
import view.component.HexagonField;

public class AttackFieldCommand extends ViewCommand {

	private HexagonField first_field;
	private HexagonField second_field; // attacked

	public AttackFieldCommand(Field first_model, Field second_model) {
		super();

		this.first_field = new HexagonField(first_model, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);

		this.second_field = new HexagonField(second_model, DrawFieldCommand.default_hex_size,
				DrawFieldCommand.default_border_width);
	}

	public AttackFieldCommand(Field first_model, Field second_model, double side_size, double border_width) {
		super();

		this.first_field = new HexagonField(first_model, side_size, border_width);

		this.second_field = new HexagonField(second_model, side_size, border_width);
	}

	public AttackFieldCommand(HexagonField first_field, HexagonField second_field) {
		super();
		this.first_field = first_field;
		this.second_field = second_field;
	}

	public void run() {

	}

}
