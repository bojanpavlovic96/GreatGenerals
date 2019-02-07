package view.command;

import java.lang.management.PlatformManagedObject;
import java.util.concurrent.ExecutorService;

import javafx.application.Platform;
import view.View;

public class ViewCommandProcessor implements QueueEventHandler {

	private ExecutorService executor = null;
	private View view;

	public ViewCommandProcessor(ExecutorService executor, View view) {

		this.executor = executor;
		this.view = view;

	}

	public void execute(CommandQueue queue) {

		while (!queue.isEmpty()) {

			final ViewCommand command = queue.dequeue();
			command.setView(this.view);

			this.executor.execute(command);

		}

	}

}
