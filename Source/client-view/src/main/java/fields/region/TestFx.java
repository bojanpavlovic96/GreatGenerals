package fields.region;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestFx extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Group root = new Group();
		Scene scene = new Scene(root);

		HexagonRegion row1 = new HexagonRegion(10, 10, 40);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		primaryStage.setMaxWidth(screen.getWidth() - 20);
		primaryStage.setMaxHeight(screen.getHeight() - 30);

		HexagonRegion prev_row = row1;
		HexagonRegion prev_col = null;
		HexagonRegion temp_hex = null;
		// root.getChildren().add(row1);

		for (int i = 0; i < 15; i++) {
			if (i == 0)
				temp_hex = row1;
			else
				temp_hex = prev_row.getNextOnY();
			root.getChildren().add(temp_hex);

			prev_col = temp_hex;
			prev_row = temp_hex;
			for (int j = 0; j < 20; j++) {
				temp_hex = prev_col.getNextOnX();
				root.getChildren().add(temp_hex);
				prev_col = temp_hex;
			}
		}

		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
