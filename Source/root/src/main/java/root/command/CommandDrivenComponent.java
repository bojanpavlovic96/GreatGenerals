package root.command;

import root.ActiveComponent;

public interface CommandDrivenComponent extends ActiveComponent {

	// TODO this method is extra, same as setCommandQueue in CommandProducer
	// BUT this method is actuallly used in gameBrain
	// refactor that usage and then remove this command (or not)
	CommandQueue getCommandQueue();

	void setCommandQueue(CommandQueue new_queue);

	CommandProcessor getCommandProcessor();

}
