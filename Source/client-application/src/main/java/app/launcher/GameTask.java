package app.launcher;

import javafx.application.Platform;
import root.ActiveComponent;
import root.controller.Controller;

public class GameTask implements Runnable, ActiveComponent {

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
		((ActiveComponent) this.controller).shutdown();
	}

}
