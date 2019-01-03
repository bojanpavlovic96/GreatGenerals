package fields.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BackroundPainterCommand extends ViewCommand {

	private Color color;

	public BackroundPainterCommand(Color color) {
		this.color = color;
	}

	public void run() {
		GraphicsContext gc = this.canvas.getGraphicsContext2D();
		gc.save();

		gc.setFill(this.color);
		gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

		gc.restore();
	}

}
