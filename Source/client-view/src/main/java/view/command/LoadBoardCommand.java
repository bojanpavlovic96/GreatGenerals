package view.command;

import java.util.List;

import javafx.application.Platform;
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

		Platform.runLater(new Runnable() {

			public void run() {

				DrawFieldCommand draw_hex_comm = null;

				// hide canvas
				((View) target_component).setCanvasVisibility(false);

				int most_x = -10;
				int most_y = -10;

				for (Field field : models) {

					if (field.getStoragePosition().getX() > most_x)
						most_x = (int) field.getStoragePosition().getX();

					if (field.getStoragePosition().getY() > most_y)
						most_y = (int) field.getStoragePosition().getY();

				}

				((View) target_component).adjustCanvasSize(new Point2D(most_x, most_y));

				ClearViewCommand clear_command = new ClearViewCommand();
				clear_command.setTargetComponent(target_component);
				clear_command.run();

				for (Field field : models) {

					draw_hex_comm = new DrawFieldCommand(field);
					draw_hex_comm.setTargetComponent(target_component);
					draw_hex_comm.run();

				}

				// show canvas
				((View) target_component).setCanvasVisibility(true);

			}
		});

	}

	@Override
	public Command getAntiCommand() {
		return new ClearViewCommand();
	}

}
