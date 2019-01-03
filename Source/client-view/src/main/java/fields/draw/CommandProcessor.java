package fields.draw;

import java.util.concurrent.ExecutorService;

import fields.command.CommandQueue;
import fields.command.QueueEventHandler;
import fields.command.ViewCommand;
import javafx.scene.canvas.Canvas;

public class CommandProcessor implements QueueEventHandler {

	private ExecutorService executor = null;
	private Canvas canvas;

	public CommandProcessor(ExecutorService executor, Canvas canvas) {

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
