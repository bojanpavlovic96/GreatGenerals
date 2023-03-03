package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;

public class StopMovingFieldOption extends FieldOption {

	public static final String Name = "stop-moving-field-option";

	public StopMovingFieldOption(Controller gameController) {
		super(StopMovingFieldOption.Name, gameController);
	}

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			return;
		}

		if (primaryField.getUnit() != null &&
				primaryField.getUnit().getMove() != null &&
				primaryField.getUnit().getMove().isMoving()) {

			primaryField.getUnit().getMove().stopMoving();
		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());
	}

	@Override
	public FieldOption getCopy() {
		return new StopMovingFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		return (selectedField.getUnit() != null &&
				selectedField.getUnit().getMove() != null &&
				selectedField.getUnit().getMove().isMoving());
	}

}