package app.launcher;

import controller.Controller;
import view.ShouldBeShutdown;

public class GameThread implements Runnable, ShouldBeShutdown {

	private Controller controller;

	public GameThread(Controller controller) {
		this.controller = controller;
	}

	public void run() {

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
