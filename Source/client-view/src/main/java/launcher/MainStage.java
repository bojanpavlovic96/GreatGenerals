package launcher;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainStage extends Stage {

	private Group root_group;
	private Scene game_scene;

	public MainStage() {

		this.root_group = new Group();
		this.game_scene = new Scene(this.root_group);

	}
	
	
	
}
