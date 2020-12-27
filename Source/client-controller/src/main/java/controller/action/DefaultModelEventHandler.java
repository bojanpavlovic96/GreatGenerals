package controller.action;

import root.controller.Controller;
import root.model.event.ModelEventArg;
import root.model.event.ModelEventHandler;

public class DefaultModelEventHandler implements ModelEventHandler {

	private Controller controller;

	public DefaultModelEventHandler(Controller controller) {
		super();

		this.controller = controller;
	}

	public void handleModelEvent(ModelEventArg arg) {

		this.controller.getServerProxy().sendIntention(arg);

	}

}
