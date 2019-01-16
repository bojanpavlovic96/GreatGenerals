
package app.launcher;

import java.util.List;

import com.rabbitmq.client.Channel;

import app.event.ConnectionReadyEvent;
import app.event.GameReadyEvent;
import app.form.InitialPage;
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

	private InitialPage first_stage;

	private Thread game_thread;
	private GameTask game_task;

	private Thread connection_thread;
	private ConnectionTask connection_task;

	private Controller controller;

	// methods

	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		// take default uri or one provided with the application arguments
		this.resolveUri();
		System.out.println("Uri resolved ...");

		// create first page for login
		this.first_stage = new InitialPage();
		this.first_stage.setOnGameReady(new GameReadyEvent() {

			public void execute(List<PlayerData> players) {

				System.out.println("Creting game thread ...");

				Communicator communicator = new Messenger(connection_task.getChannel());
				View view = new DrawingStage();
				Model model = new DataModel();

				System.out.println("\tmodel, view, communicator created ...");

				controller = new GameBrain(communicator, view, model);
				game_task = new GameTask(controller);
				game_thread = new Thread(game_task);

				System.out.println("\tstarting game thread ...");
				game_thread.start();

				System.out.println("\thiding first_stage ...");

				// hide login page
				first_stage.hide();

			}

		});
		this.first_stage.show();
		// stage created on application thread
		
		System.out.println("Creating connection thread ...");
		this.connection_task = new ConnectionTask(this.uri);
		this.connection_task.setOnConnectionReady(new ConnectionReadyEvent() {

			public void execute(Channel channel) {

				System.out.println("\tconnection ready ...");

				System.out.println("\tfirst stage channel set ...");
				first_stage.setChannel(channel);

			}
		});

		this.connection_thread = new Thread(this.connection_task);
		this.connection_thread.start();
		System.out.println("Connection thread started ...");
		// start connection thread
		// try to connect with MQ server

	}

	private void resolveUri() {

		System.out.println("Resolving uri ...");
		List<String> args = this.getParameters().getRaw();

		if (args.size() > 1) {
			System.out.println("\turi initialized from arguments ...");
			this.uri = args.get(1);
		}

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ...");

		// close connection on shutdown
		((ShouldBeShutdown) this.connection_task).shutdown();

		if (this.controller != null) {
			((ShouldBeShutdown) this.controller).shutdown();
		}

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
