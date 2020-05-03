package controller.option;

import java.beans.FeatureDescriptor;
import java.util.List;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import view.command.ClearTopLayerCommand;

public class MoveFieldOption extends FieldOption {

	public static final String Name = "move-field-option";

	public MoveFieldOption(Controller gameController) {
		super(MoveFieldOption.Name, gameController);
	}

	public MoveFieldOption(boolean enabled, Controller controller, Field primary_field) {
		super("move-field-option", enabled, controller, primary_field);

	}

	@Override
	public void run() {

		List<Field> path = this.primaryField.getUnit().getMoveType().getPath();

		if (path != null && !path.isEmpty()) {
			this.primaryField.getUnit().getMoveType().move();
		} else {
			SelectPathFieldOption select_path = new SelectPathFieldOption(true,
					this.controller,
					this.primaryField);

			select_path.setSecondaryField(this.secondaryField);
			select_path.run();
			// path is calculated, set and selected

			this.primaryField.getUnit().getMoveType().move();

		}

		super.controller.getConsumerQueue().enqueue(new ClearTopLayerCommand());

	}

	@Override
	public FieldOption getCopy() {
		return new MoveFieldOption(true, this.controller, null);
	}

	@Override
	public boolean isAdequateFor(Field field) {
		return (field.getUnit() != null &&
				field.getUnit().getMoveType() != null &&
				field.getUnit().getMoveType().getPath() != null &&
				field.getUnit().getMoveType().getPath().size() > 0);
	}

}
