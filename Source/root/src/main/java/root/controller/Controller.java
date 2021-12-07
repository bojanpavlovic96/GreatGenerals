package root.controller;

import java.util.List;

import root.command.Command;
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
		// command received from server proxy
		CommandDrivenComponent,
		// produces view commands
		CommandProducer {

	View getView();

	void setView(View newView);

	Model getModel();

	void setModel(Model newModel);

	List<FieldOption> getPossibleFieldOptions();

	// TODO replace this with getUndoneStack
	void enqueueForUndone(Command newCommand);

	CommandStack getUndoStack();

	Field getSelectedField();

	void setSelectedField(Field newField);

	void selectField(Field fieldToSelect);

}
