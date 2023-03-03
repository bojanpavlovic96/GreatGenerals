package controller.option;

import java.util.List;

import root.controller.Controller;
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

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			return;
		}

		var moveType = primaryField.getUnit().getMove();
		var oldPath = moveType.getPath();

		List<Field> pathToAdd = null;
		List<Field> pathToSelect = null;

		if (oldPath == null || oldPath.isEmpty()) {

			pathToAdd = moveType.calculatePath(controller.getModel(),
					primaryField,
					secondaryField);

			pathToAdd.add(0, primaryField);

			pathToSelect = pathToAdd.subList(1, pathToAdd.size());

		} else {

			Field lastPathField = oldPath.get(oldPath.size() - 1);
			PathFinder pathFinder = moveType.getPathFinder();
			pathToAdd = pathFinder.findPath(controller.getModel(),
					lastPathField,
					secondaryField);

			moveType.addToPath(pathToAdd);

			pathToSelect = pathToAdd;
		}

		for (Field pathField : pathToSelect) {

			var selectCommand = new SelectFieldCommand(pathField);
			super.controller.getConsumerQueue().enqueue(selectCommand);
			super.controller.getUndoStack().push(selectCommand);

		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());
	}

	@Override
	public FieldOption getCopy() {
		return new AddToPathFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field selectedField, Field targetField) {
		return (selectedField.getUnit() != null &&
				controller.isOwner(selectedField.getUnit().getOwner().getUsername()) &&
				selectedField != targetField &&
				selectedField.getUnit().getMove() != null &&
				targetField.getUnit() == null);
	}

}
