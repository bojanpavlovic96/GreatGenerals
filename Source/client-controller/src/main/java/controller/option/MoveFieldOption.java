package controller.option;

import java.util.List;

import root.command.Command;
import root.command.CommandQueue;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;

public class MoveFieldOption extends FieldOption {

	public MoveFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("move-field-option", enabled, controller, primary_field);

	}

	@Override
	public void run() {

		List<Field> path = this.primary_field.getUnit().getMoveType().getPath();

		if (path != null && !path.isEmpty()) {
			this.primary_field.getUnit().getMoveType().move();
		} else {
			SelectPathFieldOption select_path = new SelectPathFieldOption(true, this.controller,
					this.primary_field);
			select_path.setSecondaryField(this.secondary_field);
			select_path.run();
			// path is calculated, set and selected

			this.primary_field.getUnit().getMoveType().move();

		}

		// TODO next 3 lines should be moved inside fieldOption.run method
		CommandQueue viewQueue = this.controller.getConsumerQueue();
		Command hideMenuCommand = new ClearTopLayerCommand();
		viewQueue.enqueue(hideMenuCommand);

	}

	@Override
	public FieldOption getCopy() {

		return new MoveFieldOption(true, this.controller, null);

	}

}
