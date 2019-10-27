package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.Unit;
import root.model.component.option.FieldOption;

public class StopMovingFieldOption extends FieldOption {

	private Unit selectedUnit;

	public StopMovingFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("stop-moving-field-option", enabled, controller, primary_field);

		this.selectedUnit = this.primary_field.getUnit();

	}

	@Override
	public void run() {

	}

	@Override
	public FieldOption getCopy() {

		return null;
	}

}
