package view.command;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.component.field.Field;
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

		gc.setFill(Color.GRAY);

		Point2D pos = super.field.getFieldCenter();

		OptionMenu menu = super.view.getFieldMenu();

		menu.clearMenu();
		// add options for this field
		menu.setPosition(pos);
		menu.setVisible(true);

		double height = 104;
		if (menu.getHeight() > 104) {
			height = menu.getHeight();
		}

		gc.fillRect(pos.getX() + menu.getWidth(), pos.getY(), DisplayFieldInfoCommand.INFO_WIDTH, height + 1);

		// debug
		System.out.println("Info on: \nmenu: " + pos.getX()
							+ "-"
							+ pos.getY()
							+ "\ninfo: "
							+ pos.getX()
							+ menu.getWidth()
							+ "-"
							+ pos.getY()
							+ "  w: "
							+ DisplayFieldInfoCommand.INFO_WIDTH
							+ " h: "
							+ (height + 1));

		gc.restore();
	}

}
