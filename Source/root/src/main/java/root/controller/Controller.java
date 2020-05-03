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

	void setView(View new_view);

	Model getModel();

	void setModel(Model new_model);

	List<FieldOption> getPossibleFieldOptions();

	void enqueueForUndone(Command new_command);

	Field getSelectedField();

	void selectField(Field fieldToSelect);

}
