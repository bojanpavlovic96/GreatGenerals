package view;

import view.command.CommandQueue;

public interface CommandDrivenComponent {

	CommandQueue getCommandQueue();

	void setCommandQueue(CommandQueue command_queue);

}
