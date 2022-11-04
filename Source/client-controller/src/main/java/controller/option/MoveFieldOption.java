package controller.option;

import java.util.List;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;

public class MoveFieldOption extends FieldOption {

	public static final String optionName = "move-field-option";

	public MoveFieldOption(Controller gameController) {
		super(MoveFieldOption.optionName, gameController);
	}

	public MoveFieldOption(boolean enabled,
			Controller controller,
			Field primary_field) {
		super(MoveFieldOption.optionName, enabled, controller, primary_field);

	}

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			System.out.println("Primary field is null in MoveFieldOption ... ");
			// should not happen now when primary field is read from controller
			return;
		}
		if (primaryField.getUnit() == null ||
				primaryField.getUnit().getMove() == null ||
				primaryField.getUnit().getMove().getPath() == null) {
			return;
		}

		List<Field> path = primaryField.getUnit().getMove().getPath();

		if (path != null && !path.isEmpty()) {
			primaryField.getUnit().getMove().move();
		}
		// else {
		// // this will never be executed because this command is not adequate
		// // if paths == null -> isAdequateFor method
		// var selectPathOption = new SelectPathFieldOption(
		// true,
		// this.controller,
		// primaryField);

		// selectPathOption.setSecondaryField(this.secondaryField);
		// selectPathOption.run();
		// // path is calculated, set and selected

		// primaryField.getUnit().getMoveType().move();
		// }

		// just remove menu after click
		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());
	}

	@Override
	public FieldOption getCopy() {
		return new MoveFieldOption(true, this.controller, null);
	}

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null
				&& field.getUnit().getMove() != null
				&& field.getUnit().getMove().getPath() != null
				&& field.getUnit().getMove().getPath().size() > 0
				&& !field.getUnit().getMove().isMoving());
	}

}
