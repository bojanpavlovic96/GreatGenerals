
package app.launcher;

import java.util.List;

import com.rabbitmq.client.Channel;

import app.event.ConnectionReadyEvent;
import app.event.GameReadyHandler;
import app.form.InitialController;
import app.form.InitialPage;
import app.form.StartForm;
import communication.Communicator;
import controller.Controller;
import controller.GameBrain;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DataModel;
import model.Model;
import model.PlayerData;
import rabbit.Messenger;
import view.DrawingStage;
import view.ShouldBeShutdown;
import view.View;

public class Launcher extends Application {

	private String password = "lzMIQ5SJvR083poynUF6Rc8T_QPNUJow";
	private String username = "wdvozwsr";
	private String hostname = "raven.rmq.cloudamqp.com";
	private String uri = "amqp://" + username + ":" + password + "@" + hostname + "/" + username;

	// TODO delete, deprecated
	// private StartForm first_stage;
	// new way
	private InitialController initial_controller;

	private Thread game_thread;
	private GameTask game_task;

	private Thread connection_thread;
	private ConnectionTask connection_task;

	private Controller controller;

	// methods

	// is not on main (UI) thread
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

			public void execute(List<PlayerData> players) {

				System.out.println("Creting game thread ... @ Launcher.onGameReadyEvent");

				Communicator communicator = new Messenger(connection_task.getChannel());
				View view = new DrawingStage();
				Model model = new DataModel();
				// TODO new DataModel(players) should be like this

				System.out
						.println("\tmodel, view, communicator created ... @ Launcher.onGameReadyEvent");

				controller = new GameBrain(communicator, view, model);
				game_task = new GameTask(controller);
				game_thread = new Thread(game_task);

				System.out.println("\tstarting game thread ... @ Launcher.onGameReadyEvent");
				game_thread.start();

				System.out.println("\thiding first_stage ... @ Launcher.onGameReadyEvent");

				// hide login page
				initial_controller.hideInitialPage();

			}

		});
		this.initial_controller.showInitialPage();
		// stage created on application thread

		System.out.println("Creating connection thread ... @ Launcher.init");
		this.connection_task = new ConnectionTask(this.uri);
		this.connection_task.setOnConnectionReady(new ConnectionReadyEvent() {

			public void execute(Channel channel) {

				System.out.println("\tconnection ready ...");

				System.out.println("\tfirst stage channel set call ... @ Launcher.init");
				initial_controller.setCommunicationChannel(channel);

			}
		});

		this.connection_thread = new Thread(this.connection_task);
		this.connection_thread.start();
		System.out.println("Connection thread started ... @ Launcher.init");

	}

	private void resolveUri() {

		System.out.println("Resolving uri ... @ Launcher.resolveUri");
		List<String> args = this.getParameters().getRaw();

		if (args.size() > 1) {
			System.out.println("\turi initialized from arguments ...");
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
			((ShouldBeShutdown) this.connection_task).shutdown();
		}

		if (this.controller != null) {
			System.out.println("Shutting down controller ... @ Launcher.stop");
			((ShouldBeShutdown) this.controller).shutdown();
		}

		if (this.initial_controller != null) {
			System.out.println("Shutting down initial controller ... @ Launcher.stop");
			((ShouldBeShutdown) this.initial_controller).shutdown();
		}

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
