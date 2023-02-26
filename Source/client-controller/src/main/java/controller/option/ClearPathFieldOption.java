package controller.option;

import root.command.CommandQueue;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;
import view.command.UnselectFieldCommand;

public class ClearPathFieldOption extends FieldOption {

	public static final String Name = "clear-path-field-option";

	public ClearPathFieldOption(Controller gameController) {
		super(ClearPathFieldOption.Name, gameController);

	}

	@Override
	public void run() {

		var primaryField = this.getPrimaryField();
		if (primaryField == null) {
			return;
		}

		if (primaryField.getUnit() != null
				&& primaryField.getUnit().getMove() != null
				&& primaryField.getUnit().getMove().getPath() != null) {

			primaryField.getUnit().getMove().stopMoving();

			CommandQueue queue = this.controller.getConsumerQueue();

			var path = primaryField.getUnit().getMove().getPath();
			path = path.subList(1, path.size());

			for (Field singleField : path) {

				var unselectCmd = new UnselectFieldCommand(singleField);

				queue.enqueue(unselectCmd);

			}

			primaryField.getUnit().getMove().clearPath();
		}

		controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());
	}

	@Override
	public FieldOption getCopy() {
		return new ClearPathFieldOption(this.controller);
	}

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null &&
				field.getUnit().getMove() != null &&
				field.getUnit().getMove().getPath() != null &&
				field.getUnit().getMove().getPath().size() > 0);
	}

}
