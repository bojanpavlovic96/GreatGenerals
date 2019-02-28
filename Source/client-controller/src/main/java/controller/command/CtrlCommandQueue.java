package controller.command;

import java.util.LinkedList;
import java.util.Queue;

public class CtrlCommandQueue {

	private boolean running = false;

	private Queue<CtrlCommand> queue;

	private CtrlQueueEventHandler onEnqueue = null;

	public CtrlCommandQueue() {
		this.queue = new LinkedList<CtrlCommand>();
	}

	public void enqueue(CtrlCommand new_command) {

		this.queue.add(new_command);

		if (!this.running && this.onEnqueue != null) {
			this.running = true;

			this.onEnqueue.execute(this);

			this.running = false;
		}

	}

	public CtrlCommand dequeue() {

		if (!this.queue.isEmpty()) {
			return this.queue.remove();
		}

		return null;
	}

	public void setOnEnqueueEventHandler(CtrlQueueEventHandler handler) {
		this.onEnqueue = handler;
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

}
