package view.command;

import java.util.List;

import javafx.geometry.Point2D;
import root.command.Command;
import root.model.component.Field;
import root.view.View;

public class LoadBoardCommand extends Command {

	private List<Field> models;

	public LoadBoardCommand(List<Field> fields) {
		super("load-board-view-command");

		this.models = fields;

	}

	public void run() {

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

		ClearViewCommand clear_command = new ClearViewCommand();
		clear_command.setTargetComponent(targetComponent);
		clear_command.run();

		DrawFieldCommand draw_hex_comm = null;

		for (Field field : models) {

			draw_hex_comm = new DrawFieldCommand(field);
			draw_hex_comm.setTargetComponent(targetComponent);
			draw_hex_comm.run();

		}

		// show canvas
		((View) targetComponent).setCanvasVisibility(true);

	}

	@Override
	public Command getAntiCommand() {
		return new ClearViewCommand();
	}

}
