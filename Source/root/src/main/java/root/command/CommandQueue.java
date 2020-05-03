package root.command;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {

	private Queue<Command> queue;

	private CommandProcessor onEnqueue = null;

	public CommandQueue() {
		this.queue = new LinkedList<Command>();
	}

	public void enqueue(Command newCommand) {
		this.queue.add(newCommand);

		if (this.onEnqueue != null) {

			this.onEnqueue.execute(this);

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

		this.onEnqueue = commandProcessor;

		// attention if queue wasn't empty when processor is set
		// attention nonintuitive
		this.onEnqueue.execute(this);

	}

	public CommandProcessor getCommandProcessor() {
		return this.onEnqueue;
	}

}
