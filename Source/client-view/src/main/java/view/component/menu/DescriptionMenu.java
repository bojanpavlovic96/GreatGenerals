package view.component.menu;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import root.Point2D;
import root.view.menu.DescriptionItem;
import root.view.menu.FieldDescription;
import view.ResourceManager;

public class DescriptionMenu extends ListView<HBox> implements FieldDescription {

	private ObservableList<HBox> items;

	public DescriptionMenu(double width, double height) {
		super.setPrefWidth(width);
		super.setPrefHeight(height);

		items = FXCollections.observableArrayList();

		super.setItems(items);
		// super.setStyle("-fx-control-inner-background: blue;");
		// super.setStyle("-fx-control-inner-background-alt: derive(-fx-control-inner-background, 50%);");
	}

	@Override
	public void populateWith(List<DescriptionItem> descriptions) {
		this.items.clear();

		for (var desc : descriptions) {
			if (desc != null) {
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
		root.setAlignment(Pos.CENTER_LEFT);

		var TEXT_WIDTH = 200;

		var texts = new VBox();
		texts.setMaxWidth(TEXT_WIDTH);
		texts.setMinWidth(TEXT_WIDTH);
		// texts.setStyle("-fx-background-color: #ffff10"); // yellowish

		var title = new Label(desc.title);
		title.setStyle("-fx-background-color: #428daf4f"); // purple
		title.setMaxWidth(TEXT_WIDTH);
		title.setMinWidth(TEXT_WIDTH);
		texts.getChildren().add(title);

		for (var line : desc.items) {
			var label = new Label("\t" + line);
			// label.setStyle("-fx-background-color: #C0C0C0"); // gray
			texts.getChildren().add(label);
		}
		root.getChildren().add(texts);

		if (desc.iconSource != null) {
			var iconBox = new VBox();
			// iconBox.setStyle("-fx-background-color: #FFCCE5"); // pink
			// iconBox.setPadding(new Insets(0, 0, 0, 10));
			iconBox.setMinWidth(90);
			iconBox.setMaxWidth(90);
			iconBox.setMinHeight(90);
			iconBox.setMaxHeight(90);
			iconBox.setAlignment(Pos.CENTER);

			var icon = ResourceManager.getInstance().getByKey(desc.iconSource);
			var imageView = new ImageView(icon);
			imageView.setFitWidth(90);
			imageView.setFitHeight(90);

			iconBox.getChildren().add(imageView);

			root.getChildren().add(iconBox);
		}

		if (desc.action != null) {
			var actionButton = new Button();
			actionButton.setText(desc.actionText);
			actionButton.setOnMouseClicked(event -> {
				desc.action.run();
			});

			texts.getChildren().add(actionButton);
		}

		return root;
	}
}
