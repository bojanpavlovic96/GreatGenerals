package controller.option;

import java.util.List;

import root.controller.Controller;
import root.model.action.move.MoveType;
import root.model.action.move.PathFinder;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;
import view.command.SelectFieldCommand;

public class AddToPathFieldOption extends FieldOption {

	public static final String Name = "add-to-path-field-option";

	public AddToPathFieldOption(Controller gameController) {
		super(AddToPathFieldOption.Name, gameController);

	}

	public AddToPathFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("add-to-path-field-option", enabled, controller, primary_field);
		// Auto-generated constructor stub
	}

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			return;
		}

		MoveType moveType = primaryField.getUnit().getMoveType();
		List<Field> oldPath = moveType.getPath();

		List<Field> pathToAdd = null;

		if (oldPath == null || oldPath.isEmpty()) {

			pathToAdd = moveType.calculatePath(this.secondaryField);

		} else {

			Field lastPathField = oldPath.get(oldPath.size() - 1);
			PathFinder pathFinder = moveType.getPathFinder();
			pathToAdd = pathFinder.findPath(lastPathField, this.secondaryField);

			moveType.addToPath(pathToAdd);

		}

		for (Field pathField : pathToAdd) {

			var selectCommand = new SelectFieldCommand(pathField);
			// Command selectCommand = new ComplexSelectFieldCommand(field);
			super.controller.getConsumerQueue().enqueue(selectCommand);
			super.controller.enqueueForUndone(selectCommand);

		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());
	}

	@Override
	public FieldOption getCopy() {

		return new AddToPathFieldOption(true, this.controller, null);

	}

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null &&
				field.getUnit().getMoveType() != null);
	}

}
