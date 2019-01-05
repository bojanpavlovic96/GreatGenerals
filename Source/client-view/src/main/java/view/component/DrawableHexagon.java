package view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface DrawableHexagon {

	void drawOn(Canvas canvas);

	void paintHex(GraphicsContext gc, Color paint_color);

	void clearHex(GraphicsContext gc);

	void drawBattle(GraphicsContext gc);
	
}
