package view;

import view.command.CommandQueue;

public interface CommandDrivenComponent {

	CommandQueue getCommandQueue();

	// TODO maybe remove
	void setCommandQueue(CommandQueue command_queue);

}
