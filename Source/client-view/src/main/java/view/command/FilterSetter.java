package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import view.component.Hexagon;

public class FilterSetter extends ViewCommand {

	private Color filter_color;

	private Hexagon hex;

	public FilterSetter(Color color, Hexagon hex) {
		super();
		this.filter_color = color;
		this.hex = hex;
	}

	public void run() {
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();

		gc.save();

		this.filter_color = Color.rgb(100, 20, 20, 0.5);

		double[] xs = new double[6];
		double[] ys = new double[6];
		for (int i = 0; i < 6; i++) {
			xs[i] = this.hex.getCorner_points().get(i).getX();
			ys[i] = this.hex.getCorner_points().get(i).getY();
		}

		gc.setFill(filter_color);
		gc.fillPolygon(xs, ys, 6);

		gc.restore();

	}

}
