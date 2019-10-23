package controller.option;

import java.util.List;

import root.command.Command;
import root.command.CommandQueue;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearFieldCommand;

public class ClearPathFieldOption extends FieldOption {

	public ClearPathFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("clear-path-field-option", enabled, controller, primary_field);
		// Auto-generated constructor stub
	}

	@Override
	public void run() {

		List<Field> path = this.primary_field.getUnit().getMoveType().getPath();

		if (path != null && !path.isEmpty()) {

			CommandQueue queue = this.controller.getConsumerQueue();

			for (Field singleField : path) {

				Command clearCommand = new ClearFieldCommand(singleField);

				queue.enqueue(clearCommand);

			}

		}

	}

	@Override
	public FieldOption getCopy() {

		return new ClearPathFieldOption(true, this.controller, null);

	}

}
