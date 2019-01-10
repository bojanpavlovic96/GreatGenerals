package view.command;

import java.util.concurrent.ExecutorService;

import view.LayeredView;

public class ViewCommandProcessor implements QueueEventHandler {

	private ExecutorService executor = null;
	private LayeredView view;

	public ViewCommandProcessor(ExecutorService executor, LayeredView view) {

		this.executor = executor;
		this.view = view;

	}

	public void execute(CommandQueue queue) {

		while (!queue.isEmpty()) {

			ViewCommand command = queue.dequeue();
			command.setView(this.view);

			this.executor.execute(command);

		}

	}

}
