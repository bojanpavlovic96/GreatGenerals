package view.component.menu;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import root.model.component.option.FieldOption;
import root.view.menu.Menu;
import root.view.menu.MenuItem;

public class OptionMenu extends VBox implements Menu {

	public OptionMenu(double menu_width, double menu_height) {
		super.setWidth(menu_width);
		super.setHeight(menu_height);
	}

	@Override
	public void addOption(MenuItem new_option) {

		this.getChildren().add((Button) new_option);

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

	@Override
	public double getMenuWidth() {
		return super.getWidth();
	}

	@Override
	public double getMenuHeight() {
		return super.getHeight();
	}

	@Override
	public boolean isDisplayed() {
		return super.isVisible();
	}

	@Override
	public void populateWith(List<FieldOption> options) {

		clearOptions();

		for (FieldOption singleOption : options) {
			addOption(new Option(singleOption));
		}

	}

}
