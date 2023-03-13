package view.command;

import java.util.List;

import root.Point2D;
import root.command.Command;
import root.model.component.Field;
import root.view.View;

public class LoadBoardCommand extends Command {

	
	private List<Field> models;

	public LoadBoardCommand(List<Field> fields) {
		this.models = fields;
	}

	public void run() {

		System.out.println("Loading board from T: " + Thread.currentThread().getId());

		// hide canvas
		((View) targetComponent).setCanvasVisibility(false);

		int most_x = -10;
		int most_y = -10;

		for (Field field : models) {

			if (field.getStoragePosition().getX() > most_x)
				most_x = (int) field.getStoragePosition().getX();

			if (field.getStoragePosition().getY() > most_y)
				most_y = (int) field.getStoragePosition().getY();

		}

		((View) targetComponent).adjustCanvasSize(new Point2D(most_x, most_y));

		ClearViewCommand clearCommand = new ClearViewCommand();
		clearCommand.setTargetComponent(targetComponent);
		clearCommand.run();

		DrawFieldCommand drawFieldCommand = null;

		for (Field field : models) {

			drawFieldCommand = new DrawFieldCommand(field);
			drawFieldCommand.setTargetComponent(targetComponent);
			drawFieldCommand.run();

		}

		// show canvas
		((View) targetComponent).setCanvasVisibility(true);

	}

	@Override
	public Command getAntiCommand() {
		return new ClearViewCommand();
	}

}
