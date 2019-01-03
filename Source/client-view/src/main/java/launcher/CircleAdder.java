package launcher;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleAdder implements Runnable {

	private Group root;

	public void run() {

		Platform.runLater(new Runnable() {

			public void run() {
				System.out.println("Adding circle from run ...");

				root.getChildren().add(new Circle(100, 100, 100));
			}
		});

	}

	public Group getRoot() {
		return root;
	}

	public void setRoot(Group root) {
		this.root = root;
	}

}
