package root.command;

import java.util.concurrent.ExecutorService;

public class BasicCommandProcessor implements CommandProcessor {

	private ExecutorService executor = null;
	private CommandDrivenComponent command_target;

	public BasicCommandProcessor(ExecutorService executor, CommandDrivenComponent command_target) {
		super();
		this.executor = executor;
		this.command_target = command_target;
	}

	public void execute(CommandQueue command_queue) {

		while (!command_queue.isEmpty()) {

			Command command = command_queue.dequeue();
			command.setTargetComponent(this.command_target);

			this.executor.execute(command);

		}

	}

}
