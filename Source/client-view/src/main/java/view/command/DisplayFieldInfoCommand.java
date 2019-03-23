package view.command;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import root.command.Command;
import root.command.CommandDrivenComponent;
import root.model.component.Field;
import root.view.View;
import root.view.field.ViewField;
import view.component.menu.OptionMenu;

public class DisplayFieldInfoCommand extends Command {

	public static double INFO_WIDTH = 200;
	public static double INFO_HEIGHT = 100;

	// options

	private Field model;

	private ViewField view_field;

	public DisplayFieldInfoCommand(Field model /* options */) {
		super("display-field-info-view-command");

		this.model = model /* options */;

	}

	@Override
	public void setTargetComponent(CommandDrivenComponent target) {
		super.setTargetComponent(target);

		this.view_field = ((View) super.target_component).convertToViewField(this.model);

	}

	public void run() {

		GraphicsContext gc = ((View) super.target_component).getGraphicContext();
		gc.save();

		gc.setFill(Color.GRAY);

		Point2D pos = this.view_field.getFieldCenter();

		// OptionMenu menu = this.view_field.getFieldMenu();
		OptionMenu menu = null;

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
