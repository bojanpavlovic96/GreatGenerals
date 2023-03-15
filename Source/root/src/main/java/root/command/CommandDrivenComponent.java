package root.command;

public interface CommandDrivenComponent {

	CommandQueue getCommandQueue();

	void setCommandQueue(CommandQueue newQueue);

	CommandProcessor getCommandProcessor();

}
