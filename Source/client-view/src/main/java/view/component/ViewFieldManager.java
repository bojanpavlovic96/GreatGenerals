package view.component;

import javafx.geometry.Point2D;
import model.component.field.Field;

public interface ViewFieldManager {

	ViewField getViewField(Field model);

	Point2D calcStoragePosition(Point2D position);

	double getHeight();

	double getWidth();

	double getBorderWidth();

	boolean zoomIn();
	
	boolean zoomOut();
	
}
