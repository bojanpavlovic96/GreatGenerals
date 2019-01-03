package launcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DebugStage extends Stage {

	private Group root;
	private Scene scene;

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public DebugStage() {

		this.root = new Group();
		this.scene = new Scene(this.root);

		this.setScene(this.scene);

	}

	public void addCircle(CircleAdder adder) {

		adder.setRoot(this.root);

		 System.out.println("Adding to executor ...");
		 this.getExecutor().submit(adder);

	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

}
