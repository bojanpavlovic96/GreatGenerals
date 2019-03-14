package view.command;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClearTopLayerCommand extends ViewCommand {

	public ClearTopLayerCommand() {
		super();
	}

	public void run() {
		GraphicsContext gc = this.view.getTopLayerCanvas().getGraphicsContext2D();

		gc.save();

		gc.clearRect(0, 0, this.view.getCanvasWidth(), this.view.getCanvasHeight());

		this.view.getFieldMenu().setVisible(false);

		gc.restore();

	}

}
