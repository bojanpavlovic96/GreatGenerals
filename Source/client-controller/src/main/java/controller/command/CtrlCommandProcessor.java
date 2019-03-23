package controller.command;

import java.util.concurrent.ExecutorService;

import root.controller.Controller;

public class CtrlCommandProcessor implements CtrlQueueEventHandler {

	private ExecutorService executor;
	private Controller controller;

	public CtrlCommandProcessor(ExecutorService executor, Controller controller) {
		super();

		this.executor = executor;
		this.controller = controller;

	}

	public void execute(CtrlCommandQueue queue) {

		if (!queue.isEmpty()) {

			CtrlCommand command = queue.dequeue();

			command.setController(this.controller);

			this.executor.execute(command);

		}

	}

}
