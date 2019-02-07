import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CanvasLauncher extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		final int WIDTH = 600;
		final int HEIGHT = 400;

		primaryStage.setX(600);
		primaryStage.setY(200);

		primaryStage.setWidth(WIDTH);
		primaryStage.setHeight(HEIGHT);

		Group group = new Group();
		ScrollPane scroll = new ScrollPane(group);
		Scene scene = new Scene(scroll);

		Canvas canvas = new Canvas();

		canvas.setLayoutX(100);
		canvas.setLayoutY(200);

		canvas.setWidth(WIDTH + 200);
		canvas.setHeight(HEIGHT + 200);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		gc.setFill(Color.RED);
		gc.fillRect(300, 250, canvas.getWidth(), canvas.getHeight());

		group.getChildren().add(canvas);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
