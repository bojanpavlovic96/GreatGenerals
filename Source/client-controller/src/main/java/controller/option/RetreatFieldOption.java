package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class RetreatFieldOption extends FieldOption {

	public static final String name = "retreat-field-option";

	public RetreatFieldOption(Controller gameController) {
		super(RetreatFieldOption.name, gameController);
	}

	@Override
	public void run() {

	}

	@Override
	public FieldOption getCopy() {
		return null;
	}

	@Override
	public boolean isAdequateFor(Field field) {
		return false;
	}

}
