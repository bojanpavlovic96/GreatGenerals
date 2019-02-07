package view;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import view.component.menu.OptionMenu;

public interface LayeredView {

	Group getRootContainer();

	Canvas getMainCanvas();

	Canvas getTopLayerCanvas();

	OptionMenu getFieldMenu();

	Color getBackgroundColor();

	double getStageWidth();

	double getStageHeight();

	double getCanvasWidth();

	double getCanvasHeight();

}
