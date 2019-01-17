package view.component.menu;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import view.command.DisplayFieldInfoCommand;

public class FieldMenu extends OptionMenu {

	public FieldMenu() {
		super(DisplayFieldInfoCommand.INFO_WIDTH, DisplayFieldInfoCommand.INFO_HEIGHT);
	}

	public FieldMenu(double menu_width, double menu_height) {
		super(menu_width, menu_height);

	}

	public void addOption(String option) {
		super.getChildren().add(new Label(option));
	}

	public void clearMenu() {

	}

	public void setPosition(Point2D new_position) {
		super.setLayoutX(new_position.getX());
		super.setLayoutY(new_position.getY());
	}

}
