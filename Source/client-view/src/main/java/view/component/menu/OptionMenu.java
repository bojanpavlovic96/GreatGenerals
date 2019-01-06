package view.component.menu;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;

public abstract class OptionMenu extends VBox {

	public OptionMenu(double menu_width, double menu_height) {
		super.setWidth(menu_width);
		super.setHeight(menu_height);
	}

	public abstract void addOption(String option);

	public abstract void clearMenu();

	public abstract void setPosition(Point2D new_position);

}
