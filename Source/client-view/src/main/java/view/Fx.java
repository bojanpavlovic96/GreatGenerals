package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class Fx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

//		primaryStage.setTitle("Hello fx :\\");

//		primaryStage.show();

		Stage sec_stage = new CustomStage();

		sec_stage.show();

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
