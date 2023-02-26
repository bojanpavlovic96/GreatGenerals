package root.view.menu;

import java.util.List;

import root.Point2D;

public interface FieldDescription {
	void populateWith(List<DescriptionItem> items);

	boolean isVisible();

	void setPosition(Point2D position);

	void setVisible(boolean visible);
}
