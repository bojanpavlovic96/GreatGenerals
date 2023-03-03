package root.command;

import root.ActiveComponent;

public interface CommandProcessor extends ActiveComponent {

	void execute(CommandQueue commandQueue);

}
