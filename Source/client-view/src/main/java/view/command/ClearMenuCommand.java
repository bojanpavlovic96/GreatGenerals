package view.command;

import root.command.Command;
import root.view.menu.Menu;
import root.view.View;

public class ClearMenuCommand extends Command {

	public ClearMenuCommand() {
	}

	@Override
	public void run() {
		Menu fieldMenu = ((View) super.targetComponent).getMainOptionsMenu();
		fieldMenu.clearOptions();
	}

	@Override
	public Command getAntiCommand() {
		return null;
	}

}
