package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.view.View;

public class ClearViewCommand extends Command {

	public ClearViewCommand() {
	}

	public void run() {

		// clear top layer
		ClearTopLayerCommand clear_top_layer = new ClearTopLayerCommand();
		clear_top_layer.setTargetComponent(this.targetComponent);
		clear_top_layer.run();

		// clear main canvas
		GraphicsContext gc = ((View) super.targetComponent).getMainGraphicContext();
		gc.save();

		gc.setFill(((View) super.targetComponent).getBackgroundColor());

		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		gc.restore();

	}

	@Override
	public Command getAntiCommand() {
		
		return null;
	}

}
