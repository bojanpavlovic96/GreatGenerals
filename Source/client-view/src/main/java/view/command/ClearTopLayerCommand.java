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

		// gc.setFill(Color.rgb(255, 255, 255, 0));
		// gc.fillRect(0, 0, this.view.getStageWidth(), this.view.getStageHeight());

		gc.clearRect(0, 0, this.view.getStageWidth(), this.view.getStageHeight());

		this.view.getFieldMenu().setVisible(false);

		gc.restore();

	}

}
