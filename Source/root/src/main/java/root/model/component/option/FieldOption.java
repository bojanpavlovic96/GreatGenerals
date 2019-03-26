package root.model.component.option;

import root.model.component.Field;

public interface FieldOption extends Runnable {

	String getName();

	Field getField();

	void disableOption();

	void enableOption();

	boolean isEnabled();

}
