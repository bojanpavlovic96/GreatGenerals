package view.command;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.component.Field;
import view.component.menu.OptionMenu;

public class DisplayFieldInfoCommand extends ViewCommand {

	public static double INFO_WIDTH = 200;
	public static double INFO_HEIGHT = 100;

	// options

	public DisplayFieldInfoCommand(Field model /* options */) {
		super(model);

	}

	public void run() {

		GraphicsContext gc = super.view.getTopLayerCanvas().getGraphicsContext2D();
		gc.save();

		gc.setFill(Color.ALICEBLUE);

		Point2D pos = super.hex.getHexCenter();

		OptionMenu menu = super.view.getFieldMenu();

		menu.clearMenu();
		// add options for this field
		menu.setPosition(pos);
		menu.setVisible(true);

		gc.fillRect(pos.getX() + menu.getWidth(), pos.getY(), DisplayFieldInfoCommand.INFO_WIDTH,
				DisplayFieldInfoCommand.INFO_HEIGHT);

		gc.restore();
	}

}
