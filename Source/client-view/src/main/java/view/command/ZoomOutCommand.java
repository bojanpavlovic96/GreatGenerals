package view.command;

import java.util.List;

import model.component.field.Field;

public class ZoomOutCommand extends ViewCommand {

	private List<Field> fields;

	public ZoomOutCommand(List<Field> fields) {
		this.fields = fields;

	}

	public void run() {

		// view.zoomOut return true if it is possible to zoom out
		if (this.view.zoomOut()) {

			ClearViewCommand clear_view_command = new ClearViewCommand();
			clear_view_command.setView(this.view);
			clear_view_command.run();

			LoadBoardCommand load_command = new LoadBoardCommand(this.fields);
			load_command.setView(this.view);
			load_command.run();
		}
	}

}
