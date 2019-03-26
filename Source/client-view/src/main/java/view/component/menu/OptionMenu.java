package view.component.menu;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import root.view.menu.Menu;
import root.view.menu.MenuItem;

public class OptionMenu extends VBox implements Menu {

	public OptionMenu(double menu_width, double menu_height) {
		super.setWidth(menu_width);
		super.setHeight(menu_height);

	}

	@Override
	public void addOption(MenuItem new_option) {

		this.getChildren().add(new Option(new_option.getName()));

	}

	@Override
	public void removeOption(String option_name) {
		this.getChildren().removeIf(option -> ((MenuItem) option).getName().equals(option_name));
	}

	@Override
	public void clearOptions() {
		this.getChildren().clear();
	}

	@Override
	public void setPosition(Point2D position) {
		super.setLayoutX(position.getX());
		super.setLayoutY(position.getY());
	}

}
