package controller.option;

import java.util.List;

import root.command.Command;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearFieldCommand;
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

		List<Field> unitPath = primaryField.getUnit().getMoveType().getPath();
		if (unitPath != null && unitPath.size() > 0) {

			for (Field pathField : unitPath) {

				var unselectCommand = new UnselectFieldCommand(pathField);
				super.controller.getConsumerQueue().enqueue(unselectCommand);

				// ClearFieldCommand clearFieldCmd = new ClearFieldCommand(pathField);
				// DrawFieldCommand drawFieldCommand = new DrawFieldCommand(pathField);

				// super.controller.getConsumerQueue().enqueue(clearFieldCmd);
				// super.controller.getConsumerQueue().enqueue(drawFieldCommand);

			}

		}

		// second field must be without unit in order to calculate path
		for (Field pathField : primaryField
				.getUnit()
				.getMoveType()
				.calculatePath(this.secondaryField)) {

			var selectCommand = new SelectFieldCommand(pathField);
			// Command select_command = new ComplexSelectFieldCommand(field);
			super.controller.getConsumerQueue().enqueue(selectCommand);
			super.controller.enqueueForUndone(selectCommand);
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
				field.getUnit().getMoveType() != null);
	}

}
