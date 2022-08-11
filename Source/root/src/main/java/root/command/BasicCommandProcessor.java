package root.command;

import java.util.concurrent.ExecutorService;

public class BasicCommandProcessor implements CommandProcessor {

	private ExecutorService executor = null;
	private CommandDrivenComponent commandTarget;

	public BasicCommandProcessor(
			ExecutorService executor,
			CommandDrivenComponent command_target) {

		super();
		this.executor = executor;
		this.commandTarget = command_target;
	}

	public void execute(CommandQueue commandQueue) {

		while (!commandQueue.isEmpty()) {
			Command command = commandQueue.dequeue();
			command.setTargetComponent(this.commandTarget);

			this.executor.execute(command);
		}

	}

	@Override
	public void shutdown() {
		if (this.executor != null && !this.executor.isShutdown()) {
			this.executor.shutdown();
		}
	}

}
