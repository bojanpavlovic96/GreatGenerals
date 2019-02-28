package app.launcher;

import controller.Controller;
import javafx.application.Platform;
import view.ShouldBeShutdown;

public class GameTask implements Runnable, ShouldBeShutdown {

	private Controller controller;

	public GameTask(Controller controller) {
		this.controller = controller;
	}

	public void run() {

		Platform.runLater(new Runnable() {

			public void run() {

				controller.getView().show();

			}
		});

		System.out.println("Game task dead ... @ GameTask.run");

	}

	public void shutdown() {
		System.out.println("Shutting down controller ... \tin GameTask ...");
		((ShouldBeShutdown) this.controller).shutdown();
	}

}
