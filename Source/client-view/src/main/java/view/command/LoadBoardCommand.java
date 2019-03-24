package view.command;

import java.util.List;

import javafx.application.Platform;
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

				((View) target_component).setCanvasVisibility(false);

				Field right_field = models.get(0);
				Field down_field = models.get(0);

				for (Field field : models) {

					if (field.getStoragePosition().getX() >= right_field.getStoragePosition().getX()) {
						right_field = field;
					}

					if (field.getStoragePosition().getY() >= down_field.getStoragePosition().getY()) {
						down_field = field;
					}

				}

				System.out.println("For single adjust W : " + right_field.getStoragePosition());
				System.out.println("For single adjust H : " + down_field.getStoragePosition());

				// view.singleAdjust(right_field, down_field);

				for (Field field : models) {

					draw_hex_comm = new DrawFieldCommand(field);
					draw_hex_comm.setTargetComponent(target_component);
					draw_hex_comm.run();

				}

				((View) target_component).setCanvasVisibility(true);
				
			}
		});

	}

}
