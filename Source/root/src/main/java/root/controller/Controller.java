package root.controller;

import root.command.CommandDrivenComponent;
import root.command.CommandProducer;
import root.model.Model;
import root.model.event.ModelEventHandler;
import root.view.View;

public interface Controller	extends
							ModelEventHandler,
							// has server proxy
							ServerSlave,
							// command received from serve proxy
							CommandDrivenComponent,
							// produces view commands
							CommandProducer {

	View getView();

	void setView(View new_view);

	Model getModel();

	void setModel(Model new_model);

}
