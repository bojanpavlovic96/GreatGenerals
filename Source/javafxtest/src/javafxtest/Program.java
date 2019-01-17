package javafxtest;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Program extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		List<String> font_names = Font.getFontNames();

		VBox main_box = new VBox();
		ScrollPane scroll = new ScrollPane(main_box);
		scroll.setMaxHeight(700);

		Scene mainScene = new Scene(scroll);

		String example = "Quick brown fox jumps over lazy dog ";

		// for (int i = 0; i < 100; i++) {
		for (String font : font_names) {

			// String font = font_names.get(i);

			System.out.println(font);

			Label label = new Label(example + "->" + font);
			label.setPrefHeight(25);

			label.setFont(new Font(font, 15));

			main_box.getChildren().add(label);

		}

		primaryStage.setScene(mainScene);
		primaryStage.show();

	}

	public static void main(String[] args) {

		Application.launch(args);

	}

}
