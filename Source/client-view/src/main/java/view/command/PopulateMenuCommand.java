package view.command;

import java.util.List;

import root.command.Command;
import root.model.component.option.FieldOption;
import root.view.menu.Menu;
import view.component.menu.ShortMenuItem;
import root.view.View;

public class PopulateMenuCommand extends Command {

	private List<FieldOption> newOptions;

	public PopulateMenuCommand(List<FieldOption> newOptions) {
		super("populate-menu-view-command");

		this.newOptions = newOptions;
	}

	@Override
	public void run() {

		Command clearCommand = new ClearMenuCommand();
		clearCommand.setTargetComponent(super.targetComponent);
		clearCommand.run();

		Menu menu = ((View) super.targetComponent).getShortOptionMenu();

		for (FieldOption singleOption : this.newOptions) {
			menu.addOption(new ShortMenuItem(singleOption));
		}

	}

	@Override
	public Command getAntiCommand() {
		// this may not be the best antiCommand but hey ...
		return new ClearMenuCommand();
	}

}
