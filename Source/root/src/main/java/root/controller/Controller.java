package root.controller;

import java.util.List;

import root.command.CommandDrivenComponent;
import root.command.CommandProducer;
import root.model.Model;
import root.model.component.Field;
import root.model.component.option.FieldOption;
import root.model.event.ModelEventHandler;
import root.view.View;

public interface Controller extends ModelEventHandler,
		// has server proxy
		ServerSlave,
		// commands received from server proxy
		CommandDrivenComponent,
		// produces view commands
		CommandProducer {

	View getView();

	void setView(View newView);

	Model getModel();

	void setModel(Model newModel);

	List<FieldOption> getPossibleFieldOptions();

	CommandStack getUndoStack();

	Field getSelectedField();

	void setSelectedField(Field newField);

	// TODO this is useless i think
	void selectField(Field fieldToSelect);

}
