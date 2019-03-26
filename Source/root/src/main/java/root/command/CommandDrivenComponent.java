package root.command;

import root.ActiveComponent;

public interface CommandDrivenComponent extends ActiveComponent {

	CommandQueue getCommandQueue();

	void setCommandQueue(CommandQueue new_queue);

	CommandProcessor getCommandProcessor();

}
