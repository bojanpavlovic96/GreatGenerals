
package app.launcher;

import java.util.List;

import com.rabbitmq.client.Channel;

import app.event.ConnectionEventHandler;
import app.event.ConnectionReadyHandler;
import app.event.GameReadyHandler;
import app.form.ConnectionUser;
import app.form.StartForm;
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

	// private String password = "lzMIQ5SJvR083poynUF6Rc8T_QPNUJow";
	// private String username = "wdvozwsr";
	// private String hostname = "raven.rmq.cloudamqp.com";
	// private String uri = "amqp://" + username + ":" + password + "@" + hostname +
	// "/" + username;

	private static String hostname = "localhost";
	private static String uri = "amqp://" + hostname;

	private InitialController initial_controller;

	// TODO leave this application thread as game thread

	private Thread connection_thread;
	private ConnectionTask connection_task;

	// controller thread is main application thread
	private Controller controller;

	// methods

	// init() is not on main (UI) thread
	// constructor -> init() -> start()
	@Override
	public void init() throws Exception {

	}

	// running on main (UI) thread
	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		// take default uri or one provided with the application arguments
		this.resolveUri();
		System.out.println("Uri resolved ... @ Launcher.start");

		this.initial_controller = new InitialController(new StartForm());

		this.initial_controller.setOnGameReadyHandler(new GameReadyHandler() {

			public void execute(String username, String room_name) {

				// debug
				System.out.println("Creating game controller ... @ Launcher.onGameReadyEvent "
									+ "-> called from intial controller");

				ServerProxy server_proxy = new BasicServerProxy(connection_task.getChannel(),
						new JSONMessageTranslator(), username, room_name);

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
				initial_controller.hideInitialPage();

				// debug
				System.out.println("\tCreating controller ... @ Launcher.onGameReadyEvent");
				controller = new GameBrain(server_proxy, view, model);

				// debug
				System.out.println("\tShowing game view ... @ Launcher.onGameReadyEvent");
				controller.getView().show();

			}

		});
		this.initial_controller.showInitialPage();

		System.out.println("Creating connection thread ... @ Launcher.start");
		this.connection_task = new ConnectionTask(this.uri, new ConnectionEventHandler() {

			// connection ready handler
			@Override
			public void execute(ConnectionTask connectionTask) {

				// debug
				System.out
						.println("\tconnection ready ... @ Launcher.init - connectionTask.onConnecionReady");

				// debug
				System.out.println("\tfirst stage channel set call ... @ Launcher.init");
				Channel channel = connectionTask.getChannel();
				if (channel != null && channel.isOpen()) {
					initial_controller.setCommunicationChannel(connectionTask.getChannel());
				} else {

					// null is returned only if exception occurred in .getChannel function
					// in that case connection failed handler is called (inside catch block)
					// so this case is covered, just continue with execution

				}

			}
			// connection failed handler
		}, new ConnectionEventHandler() {

			@Override
			public void execute(ConnectionTask connectionTask) {

				// debug
				System.out.println("Connection failed for some reason ... @ ConnectionEventHandler");

				ConnectionUser connectionUser = (ConnectionUser) initial_controller;
				if (connectionUser != null) {
					connectionUser.handleConnectionFailure();
				}

			}
		});

		this.connection_thread = new Thread(this.connection_task);
		this.connection_thread.start();

		// debug
		System.out.println("Connection thread started ... @ Launcher.init");

	}

	private void resolveUri() {

		System.out.println("Resolving uri ... @ Launcher.resolveUri");
		List<String> args = this.getParameters().getRaw();

		if (args.size() > 1) {
			System.out.println("\tUri initialized from arguments ...");
			this.uri = args.get(1);
		}

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ... @ Launcher.start");

		// close connection on shutdown
		if (this.connection_task != null) {
			System.out.println("Closing connection ... @ Launcher.stop");
			((ActiveComponent) this.connection_task).shutdown();
		}

		if (this.controller != null) {
			System.out.println("Shutting down controller ... @ Launcher.stop");
			((ActiveComponent) this.controller).shutdown();
		}

		if (this.initial_controller != null) {
			System.out.println("Shutting down initial controller ... @ Launcher.stop");
			((ActiveComponent) this.initial_controller).shutdown();
		}

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
