package view.command;

import java.util.concurrent.ExecutorService;

import view.View;

public class ViewCommandProcessor implements ViewQueueEventHandler {

	private ExecutorService executor = null;
	private View view;

	public ViewCommandProcessor(ExecutorService executor, View view) {

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
