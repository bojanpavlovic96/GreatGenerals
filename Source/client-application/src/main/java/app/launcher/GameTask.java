package app.launcher;

import controller.Controller;
import view.ShouldBeShutdown;

public class GameTask implements Runnable, ShouldBeShutdown {

	private Controller controller;

	public GameTask(Controller controller) {
		this.controller = controller;
	}

	public void run() {
		this.controller.getView().show();
	}

	public void shutdown() {
		System.out.println("Shutting down controller ... \tin GameTask ...");
		((ShouldBeShutdown) this.controller).shutdown();
	}

}
