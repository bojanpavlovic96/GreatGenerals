package view.command;

import java.util.List;

import root.command.Command;
import root.model.component.Field;
import root.view.View;

public class ZoomInCommand extends Command {

	private List<Field> fields;

	public ZoomInCommand(List<Field> fields) {
		super("zoom-in-view-command");

		this.fields = fields;
	}

	public void run() {

		// view.zoomIn return true if it is possible to zoom in
		if (((View) super.targetComponent).zoomIn()) {

			// note board is already cleared inside the load board command
			// ClearViewCommand clearViewCommand = new ClearViewCommand();
			// clearViewCommand.setTargetComponent(super.targetComponent);
			// clearViewCommand.run();

			LoadBoardCommand load_command = new LoadBoardCommand(this.fields);
			load_command.setTargetComponent(super.targetComponent);
			load_command.run();

		}

	}

	@Override
	public Command getAntiCommand() {
		return new ZoomOutCommand(this.fields);
	}

}
