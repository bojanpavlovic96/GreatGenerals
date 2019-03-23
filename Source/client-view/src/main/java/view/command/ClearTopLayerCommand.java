package view.command;

import javafx.scene.canvas.GraphicsContext;
import root.command.Command;
import root.view.View;

public class ClearTopLayerCommand extends Command {

	public ClearTopLayerCommand() {
		super("clear-top-layer-view-command");
	}

	public void run() {
		GraphicsContext gc = ((View) super.target_component).getGraphicContext();

		gc.save();

		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		((View) super.target_component).showMenu();

		gc.restore();

	}

}
