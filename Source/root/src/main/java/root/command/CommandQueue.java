package root.command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandQueue {

	private Queue<Command> queue;

	private CommandProcessor commandProcessor = null;

	private Lock queueLock;

	public CommandQueue() {
		this.queue = new LinkedList<Command>();

		this.queueLock = new ReentrantLock();

	}

	public void enqueue(Command newCommand) {
		this.queue.add(newCommand);

		if (this.commandProcessor != null) {
			// && this.queue.size() == 1) {

			// NOTE (writen before lock implementation in this class)
			// TODO this is questionable 
			// look at basicCommandProcessor or fxCommandProcessor impl. 

			// if enqueue is called from different thread every time 
			// which is possible since commands are received from rabbit channels

			// while (!commandQueue.isEmpty()) {
			// 	Command command = commandQueue.dequeue();
			// 	command.setTargetComponent(this.commandTarget);

			// 	this.executor.execute(command);
			// }

			// this loop is gonna be run for each thread that calls enqueue
			// I might be wrong ... 
			// maybe add lock in commandProcessor.execute(), unlocked at the end of 
			// the while loop

			if (queueLock.tryLock()) {
				// try lock will return true if locking is successful
				// will not block the thread

				// if thread is already locked it is ok since at the begginign we 
				// already enqueued command, processor.execute() should traverse
				// queue and execute all queued commands

				commandProcessor.execute(this);

				queueLock.unlock();
			}
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
