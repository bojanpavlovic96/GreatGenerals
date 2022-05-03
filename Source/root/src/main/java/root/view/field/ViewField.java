package root.view.field;

import javafx.scene.canvas.GraphicsContext;
import root.Point2D;
import root.view.Color;

public interface ViewField {

	void drawOn(GraphicsContext gc);

	void paintField(GraphicsContext gc, Color color);

	void clearField(GraphicsContext gc);

	Point2D getFieldCenter();

}
