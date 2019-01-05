package controller.command;

import java.util.LinkedList;
import java.util.Queue;

public class UpdateCommandQueue {

	private Queue<Command> queue;

	private QueueEventHandler onEnqueue = null;

	public UpdateCommandQueue() {
		this.queue = new LinkedList<Command>();
	}

	public void enqueue(Command new_command) {
		this.queue.add(new_command);

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

	public void setOnEnqueueEventHandler(QueueEventHandler handler) {
		this.onEnqueue = handler;
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

}
