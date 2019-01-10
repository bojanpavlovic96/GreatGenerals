
package app.launcher;

import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import app.event.ConnectionReadyEvent;
import app.event.GameReadyEvent;
import app.page.InitialPage;
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

	// lzMIQ5SJvR083poynUF6Rc8T_QPNUJow
	// amqp://wdvozwsr:lzMIQ5SJvR083poynUF6Rc8T_QPNUJow@raven.rmq.cloudamqp.com/wdvozwsr

	private String password = "lzMIQ5SJvR083poynUF6Rc8T_QPNUJow";
	private String username = "wdvozwsr";
	private String hostname = "raven.rmq.cloudamqp.com";
	private String uri = "amqp://" + username + ":" + password + "@" + hostname + "/" + username;

	private InitialPage first_stage;

	private Thread game_thread;
	private GameThread game_task;

	private Thread connection_thread;
	private ConnectionThread connection_task;

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		// take default uri or one provided with the application arguments
		this.resolveUri();

		this.first_stage = new InitialPage("first_page_queue");
		this.first_stage.setOnGameReady(new GameReadyEvent() {

			public void execute(List<PlayerData> players) {

				System.out.println("Creting game thread ...");

				Communicator communicator = new Messenger(connection_task.getChannel());
				View view = new DrawingStage();
				Model model = new DataModel();

				controller = new GameBrain(communicator, view, model);
				game_task = new GameThread(controller);
				game_thread = new Thread(game_task);

				System.out.println("Starting game thread ...");
				game_thread.start();

				System.out.println("Hiding first_stage ...");

				// hide login page
				first_stage.hide();

			}

		});
		this.first_stage.show();

		this.connection_task = new ConnectionThread(this.uri);
		this.connection_task.setOn_connection_ready(new ConnectionReadyEvent() {

			public void execute(Channel channel) {

				first_stage.setChannel(channel);

				System.out.println("Channel set ...");
				System.out.println("Launcher->Start");

			}
		});

		this.connection_thread = new Thread(this.connection_task);
		this.connection_thread.start();
		// start connection thread
		// try to connect with MQ server

	}

	private void resolveUri() {

		List<String> args = this.getParameters().getRaw();

		if (args.size() > 1) {
			System.out.println("uri initialized from arguments ...");
			this.uri = args.get(1);
		}

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		System.out.println("Calling application stop ...");

		((ShouldBeShutdown) this.connection_task).shutdown();
		if (this.controller != null) {
			((ShouldBeShutdown) this.controller).shutdown();
		}

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
