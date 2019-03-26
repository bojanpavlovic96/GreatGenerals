package view.component;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import root.model.component.Field;
import root.view.field.ViewField;

public interface ViewFieldManager {

	ViewField getViewField(Field model);

	Point2D calcStoragePosition(Point2D position);

	Point2D calcRealPosition(Point2D storage_position);

	double getHeight();

	double getWidth();

	double getBorderWidth();

	boolean zoomIn();

	boolean zoomOut();

}
