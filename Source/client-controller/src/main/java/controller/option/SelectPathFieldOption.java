package controller.option;

import java.util.List;

import root.command.Command;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearFieldCommand;
import view.command.ClearTopLayerCommand;
import view.command.DrawFieldCommand;
import view.command.SelectFieldCommand;

public class SelectPathFieldOption extends FieldOption {

	public static final String Name = "select-path-field-option";

	public SelectPathFieldOption(Controller gameController) {
		super(SelectPathFieldOption.Name, gameController);
	}

	public SelectPathFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("select-path-field-option", enabled, controller, primary_field);
		// Auto-generated constructor stub
	}

	@Override
	public void run() {

		List<Field> unitPath = this.primaryField.getUnit().getMoveType().getPath();
		if (unitPath != null && unitPath.size() > 0) {

			for (Field singleField : unitPath) {

				ClearFieldCommand clearFieldCmd = new ClearFieldCommand(singleField);
				DrawFieldCommand drawFieldCommand = new DrawFieldCommand(singleField);

				super.controller.getConsumerQueue().enqueue(clearFieldCmd);
				super.controller.getConsumerQueue().enqueue(drawFieldCommand);

			}

		}

		// second field must be without unit in order to calculate path
		for (Field field : primaryField.getUnit().getMoveType().calculatePath(this.secondaryField)) {
			Command select_command = new SelectFieldCommand(field);
			super.controller.getConsumerQueue().enqueue(select_command);
			super.controller.enqueueForUndone(select_command);
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
