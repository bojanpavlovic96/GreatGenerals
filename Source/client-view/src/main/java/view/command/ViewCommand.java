package view.command;

import model.component.field.Field;
import view.LayeredView;
import view.View;
import view.component.ViewField;

public abstract class ViewCommand implements Runnable {

	protected View view;

	protected Field model;

	protected ViewField field;

	// methods

	public ViewCommand() {

	}

	public ViewCommand(View view, Field model) {
		this(model);

		this.view = view;

	}

	public ViewCommand(Field model) {

		// attention (view) field is initialized in setView (can't convert model to
		// viewField without view)
		this.model = model;

	}

	// getters and setters

	public void setView(View view) {

		this.view = view;
		if (this.model != null)
			this.field = this.view.convertToViewField(this.model);

	}

	public LayeredView getView() {
		return this.view;
	}

	public ViewField getField() {
		return field;
	}

}
