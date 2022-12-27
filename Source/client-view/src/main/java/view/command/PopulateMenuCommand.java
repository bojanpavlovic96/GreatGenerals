package view.command;

import java.util.List;

import root.command.Command;
import root.model.component.option.FieldOption;
import root.view.menu.Menu;
import view.component.menu.ShortMenuItem;
import root.view.View;

// doesn't seem like it is used 
// and I don't really see the point in using it ... 
// but hey ... 
public class PopulateMenuCommand extends Command {

	private List<FieldOption> newOptions;

	public PopulateMenuCommand(List<FieldOption> newOptions) {

		this.newOptions = newOptions;
	}

	@Override
	public void run() {

		Command clearCommand = new ClearMenuCommand();
		clearCommand.setTargetComponent(super.targetComponent);
		clearCommand.run();

		Menu menu = ((View) super.targetComponent).getMainOptionsMenu();

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
