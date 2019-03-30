package controller.option;

import root.controller.Controller;
import root.model.component.Field;
import root.model.component.option.FieldOption;

public class MoveFieldOption extends FieldOption {

	public MoveFieldOption(String option_name, boolean enabled, Controller controller, Field primary_field) {
		super(option_name, enabled, controller, primary_field);

	}

	@Override
	public void run() {
		if (this.primary_field.getUnit().getMoveType().getPath() != null) {
			this.primary_field.getUnit().getMoveType().move();
		} else {
			SelectPathFieldOption select_path = new SelectPathFieldOption(	"",
																			true,
																			this.controller,
																			this.primary_field);
			select_path.setSecondaryField(this.secondary_field);
			select_path.run();
			// path is calculated, set and selected

			this.primary_field.getUnit().getMoveType().move();

		}
	}

}
