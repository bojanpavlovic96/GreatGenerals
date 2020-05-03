package controller.option;

import java.io.File;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class AttackFieldOption extends FieldOption {

	public static final String Name = "attack-field-option";

	public AttackFieldOption(Controller gameController) {
		super(AttackFieldOption.Name, gameController);
	}

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

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null &&
				(field.getUnit().haveGroundAttack() || field.getUnit().haveAirAttack()));
	}

}
