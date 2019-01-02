package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomStage extends Stage {

	public CustomStage() {

		this.setTitle("custom stage title");

		this.setWidth(200);
		this.setHeight(200);

		VBox box = new VBox();
		box.getChildren().add(new Label("Label inside root :)"));

		Scene scene = new Scene(box);

		this.setScene(scene);

	}

}
