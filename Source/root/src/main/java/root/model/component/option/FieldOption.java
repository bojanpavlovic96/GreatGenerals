package root.model.component.option;

import root.controller.Controller;
import root.model.component.Field;

public abstract class FieldOption implements Runnable {

	protected String name;

	protected Controller controller;

	protected Field primaryField;
	protected Field secondaryField;

	protected boolean enabled;

	// constructors

	public FieldOption(String optionName, Controller gameController) {
		super();

		this.name = optionName;
		this.controller = gameController;

		this.enabled = false;
	}

	// note primary field will never be assigned from constructor
	public FieldOption(String optionName, boolean enabled, Controller controller, Field primaryField) {
		this(optionName, controller);

		this.enabled = enabled;
		this.controller = controller;
		this.primaryField = primaryField;
	}

	// methods

	public String getName() {
		return this.name;
	}

	public Field getPrimaryField() {
		return this.primaryField;
	}

	public void setPrimaryField(Field primary_field) {
		this.primaryField = primary_field;
	}

	public Field getSocondaryField() {
		return this.secondaryField;
	}

	public void setSecondaryField(Field secondary_field) {
		this.secondaryField = secondary_field;
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

	// TODO maybe just cloneable
	public abstract FieldOption getCopy();

}
