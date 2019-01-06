package view;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import view.component.menu.OptionMenu;

public interface LayeredView {

	Group getRootContainer();
	
	Canvas getMainCanvas();

	Canvas getTopLayerCanvas();

	OptionMenu getFieldMenu();

}
