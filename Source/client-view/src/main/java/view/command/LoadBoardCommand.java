package view.command;

import java.util.List;

import model.component.Field;

public class LoadBoardCommand extends ViewCommand {

	private List<Field> fields;

	public LoadBoardCommand(List<Field> fields) {

		this.fields = fields;

	}

	public void run() {

		DrawFieldCommand draw_hex_comm = null;

		for (Field field : this.fields) {
			draw_hex_comm = new DrawFieldCommand(field);
			draw_hex_comm.setCanvas(this.canvas);
			draw_hex_comm.run();
		}

	}

}
