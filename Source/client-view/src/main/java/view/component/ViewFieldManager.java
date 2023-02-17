package view.component;

import root.Point2D;

import root.model.component.Field;
import root.view.field.ViewField;

public interface ViewFieldManager {

	ViewField getViewField(Field model);

	Point2D calcStoragePosition(Point2D position);

	Point2D calcRealPosition(Point2D storagePosition);

	double getHeight();

	double getWidth();

	double getBorderWidth();

	boolean zoomIn();

	boolean zoomOut();

}
