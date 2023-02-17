package view.command;

import java.util.List;

import root.Point2D;
import root.command.Command;
import root.model.component.option.FieldOption;
import root.view.View;

public class ShowSubmenuCommand extends Command {

	private List<FieldOption> options;

	public ShowSubmenuCommand(List<FieldOption> options) {
		this.options = options;
	}

	@Override
	public void run() {
		var view = (View) targetComponent;

		var menu = view.getSubmenu();

		menu.clearOptions();
		menu.populateWith(options);

		var mainPos = view.getMainOptionsMenu().getPosition();
		var newPos = new Point2D(mainPos.x - menu.getMenuWidth(), mainPos.y);

		menu.setPosition(newPos);
		menu.setVisible(true);
	}

	@Override
	public Command getAntiCommand() {
		return new ClearTopLayerCommand();
	}

}
