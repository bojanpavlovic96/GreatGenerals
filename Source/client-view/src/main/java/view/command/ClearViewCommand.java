package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.view.View;

public class ClearViewCommand extends Command {

	public ClearViewCommand() {
		super("clear-view-view-command");
	}

	public void run() {

		// clear top layer
		ClearTopLayerCommand clear_top_layer = new ClearTopLayerCommand();
		clear_top_layer.setTargetComponent(this.target_component);
		clear_top_layer.run();

		// clear main canvas
		GraphicsContext gc = ((View) super.target_component).getGraphicContext();
		gc.save();

		gc.setFill(((View) super.target_component).getBackgroundColor());

		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		gc.restore();

	}

}
