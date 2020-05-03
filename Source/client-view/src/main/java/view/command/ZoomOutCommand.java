package view.command;

import java.util.List;

import root.command.Command;
import root.model.component.Field;
import root.view.View;

public class ZoomOutCommand extends Command {

	private List<Field> fields;

	public ZoomOutCommand(List<Field> fields) {
		super("zoom-in-view-command");

		this.fields = fields;
	}

	public void run() {

		// view.zoomOut return true if it is possible to zoom out
		if (((View) super.targetComponent).zoomOut()) {


			// note board is already cleared inside load board command
			// ClearViewCommand clear_view_command = new ClearViewCommand();
			// clear_view_command.setTargetComponent(super.targetComponent);
			// clear_view_command.run();

			LoadBoardCommand load_command = new LoadBoardCommand(this.fields);
			load_command.setTargetComponent(super.targetComponent);
			load_command.run();
		}
	}

	@Override
	public Command getAntiCommand() {
		return new ZoomInCommand(this.fields);
	}

}
