package controller.option;

import java.util.List;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;
import view.command.SelectFieldCommand;
import view.command.UnselectFieldCommand;

public class SelectPathFieldOption extends FieldOption {

	public static final String Name = "select-path-field-option";

	public SelectPathFieldOption(Controller gameController) {
		super(SelectPathFieldOption.Name, gameController);
	}

	public SelectPathFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super(SelectPathFieldOption.Name, enabled, controller, primary_field);
		// Auto-generated constructor stub
	}

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			return;
		}

		List<Field> unitPath = primaryField.getUnit().getMove().getPath();

		if (unitPath != null && unitPath.size() > 0) {

			for (Field pathField : unitPath) {

				var unselectCommand = new UnselectFieldCommand(pathField);
				super.controller.getConsumerQueue().enqueue(unselectCommand);

			}

		}

		// Second field must be without unit in order to calculate path.
		// That should be checked in adjustOptins on click on the destination field.
		var path = primaryField
				.getUnit()
				.getMove()
				.calculatePath(controller.getModel(), primaryField, secondaryField);

		System.out.println("FromField: " + primaryField.getStoragePosition()
				+ "\tToField: " + secondaryField.getStoragePosition());

		path.add(0, primaryField);

		for (Field pathField : path) {

			System.out.println("PathField: " + pathField.getStoragePosition());

			// TODO maybe exclude first field from selection since it should 
			// already be selected
			var selectCommand = new SelectFieldCommand(pathField);
			super.controller.getConsumerQueue().enqueue(selectCommand);
			super.controller.getUndoStack().push(selectCommand);
		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());

	}

	@Override
	public FieldOption getCopy() {

		return new SelectPathFieldOption(true, this.controller, null);

	}

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null &&
				field.getUnit().getMove() != null);
	}

}
