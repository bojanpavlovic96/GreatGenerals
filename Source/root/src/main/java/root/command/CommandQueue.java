package root.command;

public interface CommandQueue {

	void enqueue(Command newCmd);

	CommandDrivenComponent getTarget();
}
