package view.command;

import javafx.scene.canvas.GraphicsContext;

public class ClearViewCommand extends ViewCommand {

	public ClearViewCommand() {
		super();
	}

	public void run() {

		// clear top layer
		ClearTopLayerCommand clear_top_layer = new ClearTopLayerCommand();
		clear_top_layer.setView(this.view);
		clear_top_layer.run();

		// clear main canvas
		GraphicsContext gc = this.view.getMainCanvas().getGraphicsContext2D();
		gc.save();

		gc.setFill(this.view.getBackgroundColor());

		gc.fillRect(0, 0, this.view.getCanvasWidth(), this.view.getCanvasHeight());

		gc.restore();

	}

}
