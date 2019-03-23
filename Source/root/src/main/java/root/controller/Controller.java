package root.controller;

import root.ActiveComponent;
import root.model.Model;
import root.model.event.ModelEventHandler;
import root.view.View;

public interface Controller extends ModelEventHandler, ServerSlave, ActiveComponent {

	View getView();

	void setView(View new_view);

	Model getModel();

	void setModel(Model new_model);

	// from ActiveComponent
	// void shutdown()

	// from ServerSlave
	// ServerProxy getServerProxy()
	// void setServerProxy(ServerProxy new_proxy)

}
