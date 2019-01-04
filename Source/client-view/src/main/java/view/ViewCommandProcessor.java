package view;

import java.util.concurrent.ExecutorService;

import javafx.scene.canvas.Canvas;
import view.command.CommandQueue;
import view.command.QueueEventHandler;
import view.command.ViewCommand;

public class ViewCommandProcessor implements QueueEventHandler {

	private ExecutorService executor = null;
	private Canvas canvas;

	public ViewCommandProcessor(ExecutorService executor, Canvas canvas) {

		this.executor = executor;
		this.canvas = canvas;

	}

	public void execute(CommandQueue queue) {

		while (!queue.isEmpty()) {

			ViewCommand command = queue.dequeue();
			command.setCanvas(canvas);

			this.executor.execute(command);

		}

	}

}
