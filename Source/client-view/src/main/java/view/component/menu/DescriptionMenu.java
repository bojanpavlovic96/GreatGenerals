package view.component.menu;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import root.Point2D;
import root.view.menu.DescriptionItem;
import root.view.menu.FieldDescription;

public class DescriptionMenu extends ListView<HBox> implements FieldDescription {

	private ObservableList<HBox> items;

	public DescriptionMenu(double width, double height) {
		super.setPrefWidth(width);
		super.setPrefHeight(height);

		items = FXCollections.observableArrayList();

		super.setItems(items);
	}

	@Override
	public void populateWith(List<DescriptionItem> descriptions) {
		this.items.clear();
		for (var desc : descriptions) {
			if (desc != null) {
				System.out.println("DescItem NOT null: " + desc.getTitle());
				this.items.add(from(desc));
			}
		}
	}

	@Override
	public void setPosition(Point2D position) {
		super.setLayoutX(position.x);
		super.setLayoutY(position.y);
	}

	private HBox from(DescriptionItem desc) {
		var root = new HBox();
		var texts = new VBox();

		
		var title = new Label(desc.getTitle());
		texts.getChildren().add(title);

		for (var line : desc.getTextItems()) {
			texts.getChildren().add(new Label(line));
		}
		
		// // TODO hardcoded
		// if (desc.getIconSource() != null) {
			// var image = new ImageView(
		// 		new Image("/battle.jpg",
		// 				50,
		// 				50,
		// 				false,
		// 				false));
		// root.getChildren().add(image);
		// }
		
		root.getChildren().add(texts);
		
		return root;
	}
}
