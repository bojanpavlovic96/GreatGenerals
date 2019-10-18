package root.command;

import java.util.LinkedList;
import java.util.Queue;

// TODO should implement. active component 
public class CommandQueue {

	private Queue<Command> queue;

	private CommandProcessor on_enqueue = null;

	public CommandQueue() {
		this.queue = new LinkedList<Command>();
	}

	public void enqueue(Command new_command) {
		this.queue.add(new_command);

		if (this.on_enqueue != null) {

			this.on_enqueue.execute(this);

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

	public void setCommandProcessor(CommandProcessor command_processor) {

		this.on_enqueue = command_processor;

		// attention if queue wasn't empty when processor is set
		// attention nonintuitive
		this.on_enqueue.execute(this);

	}

	public CommandProcessor getCommandProcessor() {
		return this.on_enqueue;
	}

}
