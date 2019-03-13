package controller.action;

import controller.Controller;
import model.event.ModelEventArg;
import model.event.ModelEventHandler;

public class DefaultModelEventHandler implements ModelEventHandler {

	private Controller controller;

	public DefaultModelEventHandler(Controller controller) {
		super();

		this.controller = controller;
	}

	public void execute(ModelEventArg arg) {

		this.controller.getServer().sendIntention(arg);

	}

}
