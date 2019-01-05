package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.component.Field;
import model.component.GameField;
import view.component.Hexagon;

public class SelectFieldCommand extends ViewCommand {

	private Hexagon hex;

	private Color filter_color;

	private static Color default_select_color = Color.rgb(100, 10, 10, 0.5);

	public SelectFieldCommand(Field model) {
		super();

		this.hex = new Hexagon(model);
		this.filter_color = SelectFieldCommand.default_select_color;
	}

	public SelectFieldCommand(Hexagon hex) {
		super();

		this.hex = hex;
		this.filter_color = SelectFieldCommand.default_select_color;
	}

	public SelectFieldCommand(Color color, Hexagon hex) {
		super();

		this.filter_color = color;
		this.hex = hex;

	}

	public void run() {
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();

		gc.save();

		hex.paintHex(gc, this.filter_color);

		gc.restore();

	}

}
