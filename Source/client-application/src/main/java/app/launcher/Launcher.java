package app.launcher;

import com.rabbitmq.client.Channel;

import app.event.ConnectionEventHandler;
import app.event.GameReadyHandler;
import app.form.ConnectionUser;
import app.form.StartForm;
import app.resource_manager.BrokerConfig;
import communication.BasicServerProxy;
import communication.translator.JSONMessageTranslator;
import controller.GameBrain;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DataModel;
import root.ActiveComponent;
import root.communication.ServerProxy;
import root.controller.Controller;
import root.model.Model;
import root.view.View;
import view.DrawingStage;
import view.component.HexFieldManager;

public class Launcher extends Application {

	private StartPageController startPageController;

	// TODO leave this application thread as the main game thread
	private Thread connectionThread;
	private ConnectionTask connectionTask;

	// controller thread is main application thread
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

		BrokerConfig brokerConfig = BrokerConfig.loadConfig();
		if (brokerConfig != null) {
			System.out.println("Using broker config: \n" + brokerConfig.toString());
		} else {
			System.out.println("Was not able to read broker config file ... \n"
					+ "Exiting ... ");
			return;
		}

		this.startPageController = new StartPageController(
				new StartForm(),
				brokerConfig.queues,
				new GameReadyHandler() {
					public void execute(String username, String roomName) {
						// debug
						System.out.println("Starting game ... ");

						ServerProxy serverProxy = new BasicServerProxy(
								connectionTask.getChannel(),
								new JSONMessageTranslator(),
								username, roomName);

						// TODO somehow initialize resource manager
						// resources could be obtained from the server
						// values passed to the hexFieldManager should also be extracted from
						// configuration
						View view = new DrawingStage(new HexFieldManager(80, 30, 2));

						// attention controller still null at this moment
						// modelEventHandler is set from controller constructor
						// this is empty model (only timer and unit creator)
						Model model = new DataModel();

						// debug
						System.out.println("\thiding first_stage ... @ Launcher.onGameReadyEvent");
						// hide login & room page
						startPageController.hideInitialPage();

						// debug
						System.out.println("\tCreating controller ... @ Launcher.onGameReadyEvent");
						gameController = new GameBrain(serverProxy, view, model);

						// debug
						System.out.println("\tShowing game view ... @ Launcher.onGameReadyEvent");
						gameController.getView().show();

					}
				});

		this.startPageController.showInitialPage();

		System.out.println("Creating connection thread ... @ Launcher.start");
		this.connectionTask = new ConnectionTask(brokerConfig,
				new ConnectionEventHandler() {

					// connection ready handler
					@Override
					public void execute(ConnectionTask connectionTask) {

						// debug
						System.out.println("\tconnection ready ... @ Launcher.init - connectionTask.onConnecionReady");

						Channel channel = connectionTask.getChannel();
						if (channel != null && channel.isOpen()) {
							startPageController.setCommunicationChannel(connectionTask.getChannel());
						} else {

							// null is returned only if exception occurred in .getChannel function
							// in that case connection failed handler is called (inside catch block)
							// so this case is covered, just continue with execution

						}

					}
					// connection failed handler
				},
				new ConnectionEventHandler() {

					@Override
					public void execute(ConnectionTask connectionTask) {

						// debug
						System.out.println("Connection failed for some reason ... @ ConnectionEventHandler");

						ConnectionUser connectionUser = (ConnectionUser) startPageController;
						if (connectionUser != null) {
							connectionUser.handleConnectionFailure();
						}

					}

				});

		this.connectionThread = new Thread(this.connectionTask);
		this.connectionThread.start();

		// debug
		System.out.println("Connection thread started ... @ Launcher.init");

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

		if (this.startPageController != null) {
			System.out.println("Shutting down initial controller ... @ Launcher.stop");
			((ActiveComponent) this.startPageController).shutdown();
		}

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
