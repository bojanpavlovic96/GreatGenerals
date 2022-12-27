package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class AbortAttackFieldOption extends FieldOption {

	public static final String name = "abort-attack-field-option";

	public AbortAttackFieldOption(Controller gameController) {
		super(AbortAttackFieldOption.name, gameController);
	}

	@Override
	public void run() {

	}

	@Override
	public FieldOption getCopy() {
		return new AbortAttackFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field field) {
		return false;
	}

}
