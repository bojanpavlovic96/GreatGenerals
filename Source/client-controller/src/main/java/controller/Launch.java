package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.DataModel;
import view.DrawingStage;

public class Launch extends Application {

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.controller = new GameBrain(null, new DrawingStage(), new DataModel());

	}

	// used for testing drawing stage
	//
	public static void main(String[] args) {

		Application.launch(args);

	}

}
