package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.DataModel;
import view.DrawingStage;
import view.component.HexFieldManager;

public class BoardLauncher extends Application {

	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// fieldManger(field_width, field_height, border_width)
		this.controller = new GameBrain(null, new DrawingStage(new HexFieldManager(70, 30, 2)),
				new DataModel());
		// this.controller.getView().show();

	}

	// used for testing drawing stage
	//

	@Override
	public void stop() throws Exception {
		super.stop();

		this.controller.shutdown();

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
