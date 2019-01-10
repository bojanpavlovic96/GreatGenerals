package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.component.field.Field;
import view.component.Hexagon;

public class SelectFieldCommand extends ViewCommand {

	private Color filter_color;

	private static Color default_filter_color = Color.rgb(100, 10, 10, 0.5);

	public SelectFieldCommand(Field model) {
		super(model);

		this.filter_color = SelectFieldCommand.default_filter_color;
	}

	public SelectFieldCommand(Hexagon hex) {
		super(hex);

		this.filter_color = SelectFieldCommand.default_filter_color;
	}

	public SelectFieldCommand(Color color, Hexagon hex) {
		super(hex);

		this.filter_color = color;

	}

	public void run() {
		GraphicsContext gc = super.view.getMainCanvas().getGraphicsContext2D();

		gc.save();

		hex.paintHex(gc, this.filter_color);

		gc.restore();

	}

}
