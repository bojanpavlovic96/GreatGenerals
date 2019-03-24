package root.view.menu;

import javafx.geometry.Point2D;

public interface Menu {

	void addOption(MenuItem new_option);

	void removeOption(String option);

	void clearOptions();

	void setVisible(boolean visibility);

	void setPosition(Point2D position);

}
