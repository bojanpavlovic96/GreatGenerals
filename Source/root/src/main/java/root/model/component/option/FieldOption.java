package root.model.component.option;

import root.controller.Controller;
import root.model.component.Field;

public abstract class FieldOption implements Runnable {

	protected String name;

	protected Controller controller;

	protected Field primary_field;
	protected Field secondary_field;

	protected boolean enabled;

	// constructors

	public FieldOption(String option_name, Controller controller) {
		super();

		this.name = option_name;
		this.controller = controller;

	}

	public FieldOption(FieldOption old_option, Field primary_field) {
		this(old_option.name, old_option.controller);

		this.primary_field = primary_field;

	}

	// methods

	public String getName() {
		return this.name;
	}

	public Field getPrimaryField() {
		return this.primary_field;
	}

	public void setPrimaryField(Field primary_field) {
		this.primary_field = primary_field;
	}

	public Field getSocondaryField() {
		return this.secondary_field;
	}

	public void setSecondaryField(Field secondary_field) {
		this.secondary_field = secondary_field;
	}

	public void disableOption() {
		this.enabled = false;
	}

	public void enableOption() {
		this.enabled = true;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

}
