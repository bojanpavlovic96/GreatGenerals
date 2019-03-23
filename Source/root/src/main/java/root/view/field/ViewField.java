package root.view.field;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface ViewField {

	void drawOn(GraphicsContext gc);

	void paintField(GraphicsContext gc, Color color);

	void clearField(GraphicsContext gc);

	Point2D getFieldCenter();

}
