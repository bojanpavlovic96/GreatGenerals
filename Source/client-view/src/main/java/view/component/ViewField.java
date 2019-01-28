package view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface ViewField {

	void drawOn(Canvas canvas);

	void paintField(GraphicsContext gc, Color paint_color);

	void clearField(GraphicsContext gc);
	
	void drawBattle(GraphicsContext gc);
	
}
