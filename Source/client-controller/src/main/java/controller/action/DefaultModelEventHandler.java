package controller.action;

import controller.Controller;
import model.event.ModelEvent;
import model.event.ModelEventHandler;

public class DefaultModelEventHandler implements ModelEventHandler {

	private Controller controller;

	public DefaultModelEventHandler(Controller controller) {
		super();

		this.controller = controller;
	}

	public void execute(ModelEvent arg) {

		this.controller.getServer().sendIntention(arg);

	}

}
