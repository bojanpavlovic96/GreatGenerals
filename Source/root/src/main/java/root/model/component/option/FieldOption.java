package root.model.component.option;

import root.controller.Controller;
import root.model.component.Field;

public abstract class FieldOption implements Runnable {

	protected String name;

	protected Controller controller;

	protected Field secondaryField;

	protected boolean enabled;

	public FieldOption(String optionName, Controller gameController) {
		super();

		this.name = optionName;
		this.controller = gameController;

		this.enabled = false;
	}

	public String getName() {
		return this.name;
	}

	public Field getPrimaryField() {
		if (controller == null) {
			return null;
		}

		return controller.getSelectedField();
	}

	public Field getSecondaryField() {
		return this.secondaryField;
	}

	public void setSecondaryField(Field field) {
		this.secondaryField = field;
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

	// This method is never used. All fields are just referencing single instance of
	// every available option. Still issues with the current approach. 
	public abstract FieldOption getCopy();

	// TODO refactor to accept two fields (selected and focused)
	// With the current implementation setSecondaryField has to be called 
	// before isAdequate check 
	public abstract boolean isAdequateFor(Field field);

}
