package app.launcher;

import app.form.StartForm;
import app.resource_manager.BrokerConfig;
import app.server.MockupLoginServerProxy;
import communication.RabbitGameServerProxy;
import communication.translator.JSONMessageTranslator;
import controller.GameBrain;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DataModel;
import root.ActiveComponent;
import root.communication.GameServerProxy;
import root.controller.Controller;
import root.model.Model;
import root.view.View;
import view.DrawingStage;
import view.component.HexFieldManager;

public class Launcher extends Application {

	private Thread connectionThread;
	private RabbitConnectionTask connectionTask;

	private StartPageController startPageController;

	private Controller gameController;

	// METHODS

	// init() is not on main (UI) thread
	// constructor -> init() -> start()
	@Override
	public void init() throws Exception {

	}

	// running on main (UI) thread
	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		var brokerConfig = BrokerConfig.getConfig();
		if (brokerConfig != null) {
			System.out.println("Using broker config: \n" + brokerConfig.toString());
		} else {
			System.out.println("Exiting ... ");

			return;
		}

		this.connectionTask = new RabbitConnectionTask(brokerConfig);

		this.startPageController = new StartPageController(
				new StartForm(),
				new MockupLoginServerProxy(),
				// new RabbitLoginServerProxy(this.connectionTask, brokerConfig.queues),
				(String username, String roomName) -> { // game ready handler

					if (!connectionTask.isConnected()) {
						connectionTask.subscribeForEvents((connTask, eventType) -> {

						});
					}

					GameServerProxy serverProxy = new RabbitGameServerProxy(
							connectionTask.getChannel(),
							new JSONMessageTranslator(),
							username,
							roomName);

					// TODO somehow initialize resource manager
					// resources could be obtained from the server
					// values passed to the hexFieldManager should also be extracted from
					// configuration
					View view = new DrawingStage(new HexFieldManager(80, 30, 2));

					// attention controller still null at this moment
					// modelEventHandler is set from controller constructor
					// this is empty model (only timer and unit creator)
					Model model = new DataModel();

					gameController = new GameBrain(serverProxy, view, model);

					startPageController.hideInitialPage();
					gameController.getView().show();

				});

		this.connectionThread = new Thread(this.connectionTask);
		this.connectionThread.start();

		this.startPageController.showInitialPage();
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ... @ Launcher.start");

		// close connection on shutdown
		if (this.connectionTask != null) {
			System.out.println("Closing connection ... @ Launcher.stop");
			((ActiveComponent) this.connectionTask).shutdown();
		}

		if (this.gameController != null) {
			System.out.println("Shutting down controller ... @ Launcher.stop");
			((ActiveComponent) this.gameController).shutdown();
		}

		// startPageController is not active compontent
		// since the loginServerProxy is implemented
		// if (this.startPageController != null) {
		// System.out.println("Shutting down initial controller ... @ Launcher.stop");
		// ((ActiveComponent) this.startPageController).shutdown();
		// }

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
