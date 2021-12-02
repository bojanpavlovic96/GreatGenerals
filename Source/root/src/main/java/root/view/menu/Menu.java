package root.view.menu;

import java.util.List;

import javafx.geometry.Point2D;
import root.model.component.option.FieldOption;

public interface Menu {

	void addOption(MenuItem new_option);

	void removeOption(String option);

	void clearOptions();

	void setVisible(boolean visibility);

	void setPosition(Point2D position);

	double getMenuWidth();

	double getMenuHeight();

	boolean isDisplayed();

	void populateWith(List<FieldOption> options);

	void addOptionsDelimiter(String label);

}
