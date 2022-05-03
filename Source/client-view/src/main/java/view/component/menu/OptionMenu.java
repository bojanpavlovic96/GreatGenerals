package view.component.menu;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import root.Point2D;
import root.model.component.option.FieldOption;
import root.view.menu.Menu;
import root.view.menu.MenuItem;

public class OptionMenu extends ListView<MenuItem> implements Menu {

	private ObservableList<MenuItem> menuItems;

	public OptionMenu(double menuWidth, double menuHeight) {
		super.setPrefWidth(menuWidth);
		super.setPrefHeight(menuHeight);

		this.menuItems = FXCollections.observableArrayList();

		super.setItems(this.menuItems);
	}

	@Override
	public void addOption(MenuItem newOption) {
		this.menuItems.add(newOption);
	}

	@Override
	public void removeOption(String optionName) {
		this.menuItems.removeIf(option -> ((MenuItem) option)
				.getName()
				.equals(optionName));
	}

	@Override
	public void clearOptions() {
		this.menuItems.clear();
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

	@Override
	public void addOptionsDelimiter(String label) {
		addOption(new OptionsDelimiter(label));
	}

}
