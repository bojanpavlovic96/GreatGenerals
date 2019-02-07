package view.command;

import java.util.List;

import javafx.application.Platform;
import model.component.field.Field;

public class LoadBoardCommand extends ViewCommand {

	private List<Field> models;

	public LoadBoardCommand(List<Field> fields) {

		this.models = fields;

	}

	public void run() {

		Platform.runLater(new Runnable() {

			public void run() {

				DrawFieldCommand draw_hex_comm = null;

				view.setCanvasVisibility(false);

				for (Field field : models) {
					draw_hex_comm = new DrawFieldCommand(field);
					draw_hex_comm.setView(view);
					view.adjustCanvasSize(draw_hex_comm.getField());
					draw_hex_comm.run();
				}

				view.setCanvasVisibility(true);

			}
		});

	}

}
