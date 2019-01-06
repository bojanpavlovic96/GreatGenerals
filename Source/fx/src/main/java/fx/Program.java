package fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Program extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		VBox box = new VBox();
		Scene scene = new Scene(box);

		box.setLayoutX(100);
		box.setLayoutY(100);

		Button button1 = new Button("simple button 1");
		Button button2 = new Button("simple button 2");

		box.getChildren().add(button1);
		box.getChildren().add(button2);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
