package view.command;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {

	private Queue<ViewCommand> queue;

	private QueueEventHandler onEnqueue = null;

	public CommandQueue() {
		this.queue = new LinkedList<ViewCommand>();
	}

	public void enqueue(ViewCommand new_command) {
		this.queue.add(new_command);

		if (this.onEnqueue != null) {
			this.onEnqueue.execute(this);
		}

	}

	public ViewCommand dequeue() {
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
