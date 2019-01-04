
package app;

import communication.Communicator;
import controller.Controller;
import controller.GameBrain;
import javafx.application.Application;
import javafx.stage.Stage;
import model.DataModel;
import model.Model;
import rabbit.Messenger;
import view.DrawingStage;
import view.ShouldBeShutdown;

public class Launcher extends Application {

	private DrawingStage view;

	private Controller controller;
	private Communicator communicator;

	private Model model;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// ignore generated primaryStage

		this.view = new DrawingStage();
		this.view.show();

		new Thread(new Runnable() {

			public void run() {
				// thread for controller

				communicator = new Messenger();

				model = new DataModel();

				controller = new GameBrain(communicator, view, model);

			}
		}).start();

	}

	@Override
	public void stop() throws Exception {
		super.stop();

		ShouldBeShutdown up_viw = (ShouldBeShutdown) this.view;

		if (up_viw != null) {
			up_viw.shutdown();
		}

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
