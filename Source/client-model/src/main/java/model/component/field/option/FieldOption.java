package model.component.field.option;

public abstract class FieldOption {

	private String name;

	// methods

	public FieldOption(String name) {

	}

	public String getName() {
		return this.name;
	}

	public abstract void execute();

}
