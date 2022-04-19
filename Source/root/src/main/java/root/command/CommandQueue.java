package root.command;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {

	private Queue<Command> queue;

	private CommandProcessor commandProcessor = null;

	public CommandQueue() {
		this.queue = new LinkedList<Command>();
	}

	public void enqueue(Command newCommand) {
		this.queue.add(newCommand);

		if (this.commandProcessor != null) {
			// && this.queue.size() == 1) {

			// System.out.println("Executing command: " + newCommand.name);

			this.commandProcessor.execute(this);

		}

	}

	public Command dequeue() {
		if (!this.queue.isEmpty()) {
			return this.queue.remove();
		}

		return null;
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	public void setCommandProcessor(CommandProcessor commandProcessor) {

		this.commandProcessor = commandProcessor;

		// attention if queue wasn't empty when processor is set
		// attention nonintuitive
		this.commandProcessor.execute(this);

	}

	public CommandProcessor getCommandProcessor() {
		return this.commandProcessor;
	}

}
