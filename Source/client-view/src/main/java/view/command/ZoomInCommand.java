package view.command;

import java.util.List;

import model.component.field.Field;

public class ZoomInCommand extends ViewCommand {

	private List<Field> fields;

	public ZoomInCommand(List<Field> fields) {
		this.fields = fields;
	}

	public void run() {

		// view.zoomIn return true if it is possible to zoom in
		if (this.view.zoomIn()) {

			ClearViewCommand clear_view_command = new ClearViewCommand();
			clear_view_command.setView(this.view);
			clear_view_command.run();

			LoadBoardCommand load_command = new LoadBoardCommand(fields);
			load_command.setView(this.view);
			// this.view.adjustCanvasSize(load_command.getField());
			load_command.run();
		}
	}

}
