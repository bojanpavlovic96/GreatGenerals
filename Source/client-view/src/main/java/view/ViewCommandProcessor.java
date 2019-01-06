package view;

import java.util.concurrent.ExecutorService;

import javafx.scene.canvas.Canvas;
import view.command.CommandQueue;
import view.command.QueueEventHandler;
import view.command.ViewCommand;

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
