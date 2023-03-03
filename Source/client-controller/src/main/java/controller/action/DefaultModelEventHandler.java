package controller.action;

import root.controller.Controller;
import root.model.event.ClientIntention;
import root.model.event.ModelEventHandler;

public class DefaultModelEventHandler implements ModelEventHandler {

	private Controller controller;

	public DefaultModelEventHandler(Controller controller) {
		super();

		this.controller = controller;
	}

	public void handleModelEvent(ClientIntention arg) {

		this.controller.getServerProxy().sendIntention(arg);

	}

}
