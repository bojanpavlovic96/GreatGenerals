package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class AttackFieldOption extends FieldOption {

	public AttackFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("attack-field-option", enabled, controller, primary_field);
		// Auto-generated constructor stub

	}

	@Override
	public void run() {

	}

	@Override
	public FieldOption getCopy() {

		return new AttackFieldOption(true, this.controller, null);

	}

}
