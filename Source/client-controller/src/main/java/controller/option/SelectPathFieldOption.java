package controller.option;

import root.command.Command;
import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;
import view.command.SelectFieldCommand;

public class SelectPathFieldOption extends FieldOption {

	public SelectPathFieldOption(String option_name, boolean enabled, Controller controller,
			Field primary_field) {
		super(option_name, enabled, controller, primary_field);
		// Auto-generated constructor stub
	}

	@Override
	public void run() {

		for (Field field : primary_field.getUnit().getMoveType().calculatePath(this.secondary_field)) {
			Command select_command = new SelectFieldCommand(field);
			super.controller.getConsumerQueue().enqueue(select_command);
			super.controller.enqueueForUndone(select_command);
		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());

	}

}
